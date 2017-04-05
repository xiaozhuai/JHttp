# JHttp

[[中文文档](README_CN.md)] [[English README](README.md)]

JHttp是一个轻量的的http服务实现。

它可以实现基本的解析get query和post form(只支持x-www-form-urlencoded格式)，
并且提供了一个简单的路由。

不要尝试在要求高效率的项目中使用它，因为它是基于多线程的，没有事件驱动的支持。(apache也是多线程模型)

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
```