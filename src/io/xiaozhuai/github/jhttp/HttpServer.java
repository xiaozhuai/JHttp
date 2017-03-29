package io.xiaozhuai.github.jhttp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                socket.setKeepAlive(true);
                executorService.execute(new HttpHandler(socket, this));
            } catch (IOException e) {
                e.printStackTrace();
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
        String path = request.getPath();
        IHttpRouter router = null;
        if(routerMap.containsKey(path) && (router = routerMap.get(path))!=null ){
            router.onRoute(request, response);
        }else{
            response.setStatus(404);
        }
    }
}
