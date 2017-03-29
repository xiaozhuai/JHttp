package io.xiaozhuai.github.jhttp;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public interface IHttpRouter {
    void onRoute(HttpRequest request, HttpResponse response);
}