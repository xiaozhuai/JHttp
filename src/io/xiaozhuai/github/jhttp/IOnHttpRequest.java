package io.xiaozhuai.github.jhttp;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public interface IOnHttpRequest {
    void onRequest(HttpRequest request, HttpResponse response);
}
