# JHttp

[[中文文档](README_CN.md)] [[English README](README.md)]

* author: xiaozhuai

* email: 798047000@qq.com

JHttp是一个轻量的的http服务实现。

它可以实现基本的解析get query和post form(只支持x-www-form-urlencoded格式)，
并且提供了一个简单的路由。

不要尝试在要求高效率的项目中使用它，因为它是基于线程池的，没有事件驱动的支持。(apache也是多线程模型)

但是你可以在安卓应用中使用它，来开启一个功能简单的http服务。

**注意** 

监听小于1024的端口需要root用户权限。

如果你在安卓环境下使用，你不能在UI线程开启服务。

# 例子

查看 `Main.java`

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

# 注意事项

对于 `router` 和 `regeRouter`，不要使用类成员变量来传递参数，这会导致多线程竞争的发生，
(因为所有命中该router的请求实际是由同一个实例去处理的)

当然，如果你使用java8，那么更好的方式是使用lambda，这样就不需要考虑这个问题。

`router` 和 `regexRouter` 被设计为处理一些非常简单的请求，因此才会使用lambda或者匿名内部类实例，
因为你不会想把所有这些简单的router都作为一个类，单独写到一个java文件里。如果涉及到复杂的业务逻辑，
更好的选择是使用 `controller` 而不是 `router`.

对于 `controller`, 每一个命中的请求都会使用一个新的实例去处理。因此你可以放心的使用类成员，这不会造成竞争。

# 鸣谢

感谢以下成员的为JHttp所作的贡献。 : )

* @py庄稼汉(weihai4099)