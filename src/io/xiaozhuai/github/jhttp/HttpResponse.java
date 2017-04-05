package io.xiaozhuai.github.jhttp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public class HttpResponse {

    private StringBuilder headerBuilder = new StringBuilder();

    private List<byte[]> bodyFrames = new ArrayList<>();
    private long contentLength = 0;

    private boolean bodyIsFile = false;
    private FileInputStream fileInput;

    private int code;
    private String status;
    private List<String> headerLines = new ArrayList<>();

    public HttpResponse(){
        setStatus(200);
    }

    public HttpResponse(int _code){
        setStatus(_code);
    }

    public void setStatus(int _code){
        code = HttpStatus.statusSupported(_code) ? _code : 500;
        status = HttpStatus.getStatusMsg(code);
    }

    public List<byte[]> getBodyFrames(){
        return bodyFrames;
    }

    public String getHeader(){
        headerBuilder.append(String.format("HTTP/1.1 %d %s\r\n", code, status));
        if(contentLength!=0) header("Content-Length", Long.toString(contentLength));
        header("Connection: keep-alive");
        header("Server", Version.name+"/"+Version.version);
        for(int i=0; i<headerLines.size(); i++){
            headerBuilder.append(headerLines.get(i));
        }
        headerBuilder.append("\r\n");
        return headerBuilder.toString();
    }

    public void header(String key, String value){
        headerLines.add(key+": "+value+"\r\n");
    }

    public void header(String line){
        if(line.endsWith("\r\n")){
            headerLines.add(line);
        }else{
            headerLines.add(line+"\r\n");
        }
    }

    public void contentType(String type){
        header("Content-Type", type);
    }

    public HttpResponse append(String text){
        byte[] bytes = text.getBytes();
        return append(bytes);
    }

    public HttpResponse append(byte[] bytes){
        bodyFrames.add(bytes);
        contentLength += bytes.length;
        return this;
    }

    public void file(String path) throws IOException {
        file(new File(path));
    }

    public void file(File file) throws IOException {
        file(new FileInputStream(file));
    }

    public void file(FileInputStream in) throws IOException {
        bodyIsFile = true;
        contentLength = in.getChannel().size();
        fileInput = in;
    }

    public boolean isFile(){
        return bodyIsFile;
    }

    public InputStream getFileInput(){
        return fileInput;
    }

}
