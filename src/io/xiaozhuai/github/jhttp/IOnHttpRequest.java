package io.xiaozhuai.github.jhttp;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
interface IOnHttpRequest {
    void onRequest(HttpRequest request, HttpResponse response);
}
