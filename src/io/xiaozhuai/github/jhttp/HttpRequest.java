package io.xiaozhuai.github.jhttp;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolFamily;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public class HttpRequest {

    private String header;

    private Map<String, List<String>> headerList = new HashMap<>();

    private String body;

    private Map<String, String> getQuery = new HashMap<>();

    private Map<String, String> postQuery = new HashMap<>();

    private String method;

    private String host = "127.0.0.1";
    private String path = "/";
    private String getQueryText = "";

    private String protocolVersion;


    public HttpRequest(){

    }

    public void setHeader(String headerText){
        header = headerText;
        parseHeader();
    }

    public void setBody(String bodyText){
        body = bodyText;
        if(method.equals("POST")){
            parsePostQuery();
        }
    }

    private void parseHeader(){
        String[] headerTextList = header.split("\r\n");

        String[] tmp;

        /**
         * first line, method url version
         */
        tmp = headerTextList[0].split(" ");
        method = tmp[0];
        parseUrl(tmp[1]);
        protocolVersion = tmp[2];

        /**
         * others line
         */
        for(int i=1; i<headerTextList.length; i++){
            if(headerTextList[i].equals("")) continue;
            tmp = headerTextList[i].split(": ", 2);
            if(tmp.length==2){
                String key = tmp[0];
                String value = tmp[1];
                if(headerList.containsKey(key)){
                    headerList.get(key).add(value);
                }else{
                    List<String> valList = new ArrayList<>();
                    valList.add(value);
                    headerList.put(key, valList);
                }
            }
        }

        if(headerList.containsKey("Host")){
            host = headerList.get("Host").get(0);
        }
    }

    private void parseUrl(String u){
        int splitIndex = u.indexOf("?");
        if(splitIndex>=0){
            path = u.substring(0, splitIndex);
            getQueryText = u.substring(splitIndex+1);
            parseGetQuery();
        }else{
            path = u;
        }
    }

    private void parsePostQuery(){
        parseQuery(postQuery, body);
    }

    private void parseGetQuery(){
        parseQuery(getQuery, getQueryText);
    }

    private void parseQuery(Map<String, String> map, String text){
        String[] list = text.split("&");
        for(int i=0; i<list.length; i++){
            String[] kv = list[i].split("=", 2);
            if(kv.length==2){
                try {
                    kv[0] = URLDecoder.decode(kv[0], "UTF-8");
                    kv[1] = URLDecoder.decode(kv[1], "UTF-8");
                    map.put(kv[0], kv[1]);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHost() {
        return host;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Map<String, List<String>> headers() {
        return headerList;
    }

    public Map<String, String> gets() {
        return getQuery;
    }

    public Map<String, String> posts() {
        return postQuery;
    }

    public List<String> header(String key){
        if(headerList.containsKey(key)){
            return headerList.get(key);
        }else{
            return null;
        }
    }

    public String get(String key, String defaultValue){
        if(getQuery.containsKey(key)){
            return getQuery.get(key);
        }else{
            return defaultValue;
        }
    }

    public String post(String key, String defaultValue){
        if(postQuery.containsKey(key)){
            return postQuery.get(key);
        }else{
            return defaultValue;
        }
    }
}
