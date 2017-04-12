package io.xiaozhuai.github.jhttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaozhuai on 17/4/12.
 */
public class HttpConfig {

    public interface CustomPageAction{
        void onCustomPage(HttpRequest request, HttpResponse response);
    }

    public static Map<Integer, CustomPageAction> customPageActionHashMap = new HashMap<>();

    public static void addCustomPageAction(int statusCode, CustomPageAction action){
        customPageActionHashMap.put(statusCode, action);
    }

    public static void removeCustomPageAction(int statusCode){
        if(customPageActionHashMap.containsKey(statusCode)){
            customPageActionHashMap.remove(statusCode);
        }
    }


}
