package io.xiaozhuai.github.jhttp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void addRouter(String routerPath, IHttpRouter action){
        routerMap.put(routerPath, action);
    }

    public void removeRouter(String routerPath){
        routerMap.remove(routerPath);
    }

    @Override
    public void onRequest(HttpRequest request, HttpResponse response) {
        HttpLog.I("%s %s", request.getMethod(), request.getUrl());
        String path = request.getPath();
        IHttpRouter router = null;
        if(routerMap.containsKey(path) && (router = routerMap.get(path))!=null ){
            router.onRoute(request, response);
        }else{
            response.setStatus(404);
        }
    }
}
