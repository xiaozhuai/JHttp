package io.xiaozhuai.github.jhttp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public class HttpServer implements IOnHttpRequest{
    private int port;
    private ServerSocket serverSocket = null;
    private ExecutorService executorService;
    private static final int POOL_MULTIPLE = 4;

    public HttpServer(int _port) throws IOException {
        port = _port;
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors() * POOL_MULTIPLE);
        serverSocket = new ServerSocket(port);
    }

    public HttpServer() throws IOException {
        this(80);
    }

    public void serv() {
        HttpLog.I("Listening 0.0.0.0:%d ...", port);
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                HttpLog.D("New socket accepted");
                socket.setKeepAlive(true);
                executorService.execute(new HttpHandler(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
                executorService.shutdown();
                try {
                    //60s后查看是否结束任务池,
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    	executorService.shutdownNow(); 
                    }
				} catch (InterruptedException ie) {
					executorService.shutdownNow();
				    Thread.currentThread().interrupt();
				}

            }
        }
    }

    private Map<String, IHttpRouter> routerMap = new HashMap<>();
    private Map<String, IHttpRouter> regexRouterMap = new HashMap<>();

    public void addRouter(String routerPath, IHttpRouter action){
        routerMap.put(routerPath, action);
    }

    public void addRouterRegex(String regex, IHttpRouter action){
        regexRouterMap.put(regex, action);
    }

    public void removeRouter(String routerPath){
        routerMap.remove(routerPath);
    }

    @Override
    public void onRequest(HttpRequest request, HttpResponse response) {
        HttpLog.I("%s %s", request.getMethod(), request.getUrl());
        String path = request.getPath();
        IHttpRouter router = null;

        // 优先全路径匹配路由
        if(routerMap.containsKey(path) && (router = routerMap.get(path))!=null ){
            router.onRoute(request, response);
            return;
        }

        // 正则匹配路由
        for (Map.Entry<String, IHttpRouter> entry : regexRouterMap.entrySet()) {
            String regex = entry.getKey();
            if(path.matches(regex)){
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(path);
                matcher.find();
                request.setPathinfo(matcher);
                entry.getValue().onRoute(request, response);
                return;
            }
        }

        // 无匹配结果则404
        response.setStatus(404);
    }
}
