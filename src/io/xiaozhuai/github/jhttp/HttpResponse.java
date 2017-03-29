package io.xiaozhuai.github.jhttp;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public class HttpResponse {

    private static final String HEADER_FORMAT = "HTTP/1.1 %d %s\r\nContent-Type: text/html\r\nContent-Length: %d\r\nConnection: keep-alive\r\n\r\n";
    private StringBuilder body = new StringBuilder();

    private int code;
    private String status;

    public HttpResponse(){
        setStatus(200);
    }

    public HttpResponse(int _code){
        setStatus(_code);
    }

    public void setStatus(int _code){
        code = _code;
        switch (code){
            case 200:
                status = "OK";
                break;
            case 403:
                status = "Forbidden";
                break;
            case 404:
                status = "Not Found";
                break;
            case 500:
                status = "Internal Server Error";
                break;
        }
        if(code!=200){
            body.append(code+" "+status);
        }
    }

    public byte[] getBytes(){
        return toString().getBytes();
    }

    public String toString(){
        StringBuilder data = new StringBuilder()
                .append(String.format(HEADER_FORMAT, code, status, body.toString().getBytes().length))
                .append(body);
        return data.toString();
    }

    public HttpResponse append(String text){
        body.append(text);
        return this;
    }

}
