package io.xiaozhuai.github.jhttp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


/**
 * @author : xiaozhuai
 * @date : 17/2/10
 */
public class HttpHandler implements Runnable {

    private Socket socket;
    private IOnHttpRequest mOnRequest;

    public HttpHandler(Socket socket, IOnHttpRequest l) {
        this.socket = socket;
        this.mOnRequest = l;
    }

    @Override
    public void run() {
        handle();
    }

    private void handle() {
        InputStream in = null;
        OutputStream out = null;

        try {

            /**
             * get the input and output stream
             */
            in = socket.getInputStream();
            out = socket.getOutputStream();

            HttpRequest request = new HttpRequest();

            /**
             * request data
             */
            StringBuilder headerBuilder = new StringBuilder();
            StringBuilder bodyBuilder = new StringBuilder();
            String requestHeader;
            String requestBody;


            /**
             * buffer
             */
            int bufferSize = 1024;
            byte[] block = new byte[bufferSize];


            byte[] ring = new byte[]{0,0,0,0};
            int headerIndex = 0;
            byte[] oneByte = new byte[1];

            /**
             * read header
             */
            while (true){
                if(in.read(oneByte)==-1){
                    out.write(new HttpResponse(500).getHeader().getBytes());
                    out.flush();
                    return;
                }else{
                    headerBuilder.append((char)oneByte[0]);
                    ring[headerIndex] = oneByte[0];

                    if(
                            ring[headerIndex%4] == '\n'
                            && ring[(headerIndex+3)%4] == '\r'
                            && ring[(headerIndex+2)%4] == '\n'
                            && ring[(headerIndex+1)%4] == '\r'
                            ){
                        break;
                    }
                    headerIndex ++;
                    headerIndex = headerIndex % 4;
                }
            }
            requestHeader = headerBuilder.toString();
            request.setHeader(requestHeader);
            HttpLog.D("Http header complete, "+requestHeader);

            long contentLength = 0;
            List<String> tmp = request.header("Content-Length");
            if(tmp==null || tmp.size()==0) contentLength = 0;
            else contentLength = Long.parseLong(tmp.get(0));

            while (contentLength>0){
                int len;
                len=in.read(block);
                if(len==-1){
                    out.write(new HttpResponse(500).getHeader().getBytes());
                    out.flush();
                    return;
                }else{
                    contentLength-=len;
                    bodyBuilder.append(new String(block, 0, len));
                }
            }
            requestBody = bodyBuilder.toString();
            request.setBody(requestBody);
            HttpLog.D("Http body complete");

            /**
             * generate a new response
             */
            HttpResponse response =  new HttpResponse();

            /**
             * callback to get response data
             */
            mOnRequest.onRequest(request, response);
            int statusCode = response.getStatus();
            if(statusCode!=200 && statusCode!=204 && HttpConfig.customPageActionHashMap.containsKey(statusCode)){
                HttpConfig.customPageActionHashMap.get(statusCode).onRoute(request, response);
            }

            byte[] responseHeader = response.getHeader().getBytes();
            out.write(responseHeader);
            if(response.isFile()){
                byte[] fileBuffer = new byte[10240];
                InputStream fileInput = response.getFileInput();
                int tmpLen;
                while((tmpLen=fileInput.read(fileBuffer))!=-1){
                    out.write(fileBuffer, 0, tmpLen);
                }
            }else{
                List<byte[]> responseBodyFrames = response.getBodyFrames();
                for(int i=0; i<responseBodyFrames.size(); i++){
                    out.write(responseBodyFrames.get(i));
                }
            }
            out.flush();


        } catch (Exception e) {
            e.printStackTrace();
            try{
                out.write(new HttpResponse(500).getHeader().getBytes());
                out.flush();
            }catch (Exception e2){
                e2.printStackTrace();
            }
        } finally {
            try {
                if(in!=null)
                    in.close();
                if(out!=null)
                    out.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}