package io.xiaozhuai.github;

import io.xiaozhuai.github.jhttp.HttpRequest;
import io.xiaozhuai.github.jhttp.HttpResponse;
import io.xiaozhuai.github.jhttp.HttpServer;
import io.xiaozhuai.github.jhttp.IHttpRouter;

import java.io.IOException;

public class Main {

    static HttpServer server;
    static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            server = new HttpServer(PORT);
            server.addRouter("/", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    response.setStatus(200);
                    response.append("hello");
                }
            });
            server.addRouter("/user", new IHttpRouter() {
                @Override
                public void onRoute(HttpRequest request, HttpResponse response) {
                    response.setStatus(200);
                    String user = request.get("user", "");
                    response.append("hello, "+user);
                }
            });
            System.out.println("Listen on 0.0.0.0:"+PORT);
            server.service();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
