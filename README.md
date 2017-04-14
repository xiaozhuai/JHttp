# JHttp

[[中文文档](README_CN.md)] [[English README](README.md)]

* author: xiaozhuai

* email: 798047000@qq.com

JHttp is a tiny http server implementation.

It's can parse get query and post form(only x-www-form-urlencoded supported)，
and it provided a simple router.

Do not use it in the project which need high efficiency, 
because it's base on thread-pool and no event-driven support.（same as apache）

But you can use it on an Android application.

**WARNING** 

port < 1024 need root user authority.

if you use it on Android, you can not run server on UI thread.

# Example

see `Main.java`

```java
public class Main {

    static HttpServer server;
    static final int PORT = 8080;

    public static void main(String[] args) {
        HttpLog.setLogLevel(HttpLog.LOG_LEVEL_DEBUG); //LOG_LEVEL_INFO by default

        // when 404 occured, use this to define a custom err page is a good idea
        HttpConfig.addCustomPageAction(404, new IHttpRouter() {
            @Override
            public void onRoute(HttpRequest request, HttpResponse response) {
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
                    response.append(action+" an article");
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

            //example Controller
            server.addController("/comment/(\\w+)", ExampleCommentController.class);

            server.serv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

# Gratitude

Thanks to these nice people's work. : )

* @py庄稼汉(weihai4099)