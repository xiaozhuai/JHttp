# JHttp

[[中文文档](README_CN.md)] [[English README](README.md)]

JHttp is a tiny http server implementation.

It's can parse get query and post form(only x-www-form-urlencoded supported)，
and it provided a simple router.

Do not use it in the project which need high efficiency, 
because it's single thread and no event-driven support.

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
```