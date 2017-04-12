package io.xiaozhuai.github;

import io.xiaozhuai.github.jhttp.*;

import java.io.IOException;

public class Main {

    static HttpServer server;
    static final int PORT = 8080;

    public static void main(String[] args) {
        HttpLog.setLogLevel(HttpLog.LOG_LEVEL_DEBUG); //LOG_LEVEL_INFO by default

        // when 404 occured, use this to define a custom err page is a good idea
        HttpConfig.addCustomPageAction(404, new HttpConfig.CustomPageAction() {
            @Override
            public void onCustomPage(HttpRequest request, HttpResponse response) {
                response.append("404 Not Found, Powered by JHttp");
            }
        });

        try {
            server = new HttpServer(PORT);

            // example route
            server.addRouter("/", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    response.append("hello");
                }
            });

            // example regex route
            server.addRouterRegex("/article/(\\w+)", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    String action = request.getPathinfo().group(1); // (\\w+) maybe add, delete, read, etc...
                    response.append(action+" an artical");
                }
            });

            // example get query
            server.addRouter("/user", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    String user = request.get("user", "");
                    response.append("hello, "+user);
                }
            });

            // example response file
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
