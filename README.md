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
```

# Warning
For `router` or `regexRouter`, remember do not use member variables to transfer function parameter, 
it will cause multi thread competition. 

Of cause, if you use java 8, lambda is a good idea. With lambda, you do not need consider this.

For `controller`, every time the router hit the controller, it will make a new instance of controller, 
so you can do anything, include use member variables to transfer function parameter.

# Gratitude

Thanks to these nice people's work. : )

* @py庄稼汉(weihai4099)