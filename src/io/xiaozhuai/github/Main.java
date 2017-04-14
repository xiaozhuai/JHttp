package io.xiaozhuai.github;

import io.xiaozhuai.github.jhttp.*;

import java.io.IOException;

public class Main {

    static HttpServer server;
    static final int PORT = 8080;

    public static void main(String[] args) {
        HttpLog.setLogLevel(HttpLog.LOG_LEVEL_DEBUG); //LOG_LEVEL_INFO by default

        // when 404 occured, use this to define a custom err page is a good idea
        HttpConfig.addCustomPageAction(404, (request, response) -> response.append("404 Not Found, Powered by JHttp"));

        try {
            server = new HttpServer(PORT);

            // example route, anonymous inner class instance or lambda(on java 8)
            server.addRouter("/", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    response.append("hello");
                }
            });

            // example regex route
            server.addRouterRegex("/article/(\\w+)", (request, response) -> {
                String action = request.getPathinfo().group(1); // (\\w+) maybe add, delete, read, etc...
                response.append(action+" an article");
            });

            // example get query
            server.addRouter("/user", (request, response) -> {
                String user = request.get("user", "");
                response.append("hello, "+user);
            });

            // example response file
            server.addRouter("/file", (request, response) -> {
                try {
                    response.contentType("text/plain");
                    response.file("/Users/xiaozhuai/Desktop/test.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            //example Controller, "?<action>" marked group will be action method name
            server.addController("/comment/(?<action>\\w+)", ExampleCommentController.class);

            server.serv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
