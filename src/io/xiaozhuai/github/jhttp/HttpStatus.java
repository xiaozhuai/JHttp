package io.xiaozhuai.github.jhttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaozhuai on 17/4/5.
 */
public class HttpStatus {
    public static Map<Integer, String> status = new HashMap<>();

    static {
        status.put(100, "Continue");
        status.put(101, "Switching Protocols");
        status.put(200, "OK");
        status.put(201, "Created");
        status.put(202, "Accepted");
        status.put(203, "Non");
        status.put(204, "No Content");
        status.put(205, "Reset Content");
        status.put(206, "Partial Content");
        status.put(300, "Multiple Choices");
        status.put(301, "Moved Permanently");
        status.put(302, "Found");
        status.put(303, "See Other");
        status.put(304, "Not Modified");
        status.put(305, "Use Proxy");
        status.put(306, "Unused");
        status.put(307, "Temporary Redirect");
        status.put(400, "Bad Request");
        status.put(401, "Unauthorized");
        status.put(402, "Payment Required");
        status.put(403, "Forbidden");
        status.put(404, "Not Found");
        status.put(405, "Method Not Allowed");
        status.put(406, "Not Acceptable");
        status.put(407, "Proxy Authentication Required");
        status.put(408, "Request Time");
        status.put(409, "Conflict");
        status.put(410, "Gone");
        status.put(411, "Length Required");
        status.put(412, "Precondition Failed");
        status.put(413, "Request Entity Too Large");
        status.put(414, "Request");
        status.put(415, "Unsupported Media Type");
        status.put(416, "Requested range not satisfiable");
        status.put(417, "Expectation Failed");
        status.put(500, "Internal Server Error");
        status.put(501, "Not Implemented");
        status.put(502, "Bad Gateway");
        status.put(503, "Service Unavailable");
        status.put(504, "Gateway Time");
        status.put(505, "HTTP Version not supported");
    }

    public static String getStatusMsg(int statusCode){
        if(status.containsKey(statusCode)){
            return status.get(statusCode);
        }else{
            return status.get(500);
        }
    }

}
