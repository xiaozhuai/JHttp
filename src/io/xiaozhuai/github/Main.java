package io.xiaozhuai.github;

import io.xiaozhuai.github.jhttp.*;

import java.io.IOException;

public class Main {

    static HttpServer server;
    static final int PORT = 8080;

    public static void main(String[] args) {
        HttpLog.setLogLevel(HttpLog.LOG_LEVEL_DEBUG); //LOG_LEVEL_INFO by default
        try {
            server = new HttpServer(PORT);
            server.addRouter("/", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    response.append("hello");
                }
            });
            server.addRouter("/user", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    String user = request.get("user", "");
                    response.append("hello, "+user);
                }
            });
            server.addRouter("/file", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    try {
                        response.contentType("text/plain");
                        response.file("/Users/xiaozhuai/Desktop/test.txt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            server.serv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
