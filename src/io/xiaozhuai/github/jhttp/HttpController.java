package io.xiaozhuai.github.jhttp;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

/**
 * Created by xiaozhuai on 17/4/13.
 */
public class HttpController implements IHttpRouter{
    protected HttpRequest request;
    protected HttpResponse response;


    @Override
    public void onRoute(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
        Matcher pathinfo = request.getPathinfo();
        String action = null;
        if (pathinfo != null && pathinfo.groupCount() != 0)
            action = pathinfo.group("action");
        if (action == null) {
            response.setStatus(404);
            return;
        }
        try {
            Method method = getClass().getMethod(action);
            method.invoke(this);
        } catch (Exception e) {
            response.setStatus(404);
        }

    }

}
