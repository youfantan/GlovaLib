package glovalib.network;

import glovalib.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HttpClient {
    public static class HttpResponse {
        public HttpResponse(String url, Hashtable<String, String> header, byte[] body, int responseCode) {
            this.url = url;
            this.header = header;
            this.body = body;
            this.responseCode = responseCode;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Hashtable<String, String> getHeader() {
            return header;
        }

        public void setHeader(Hashtable<String, String> header) {
            this.header = header;
        }

        public byte[] getBody() {
            return body;
        }

        public void setBody(byte[] body) {
            this.body = body;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        private String url;
        private Hashtable<String,String> header;
        private byte[] body;
        private int responseCode;
    }
    public HttpResponse doGet(@NotNull String _url, @Nullable Hashtable<String,String> optionalHeaders, @Nullable byte[] body) throws IOException {
        URL url=new URL(_url);
        HttpURLConnection connection= (HttpURLConnection) (url).openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        if (optionalHeaders!=null){
            for (Map.Entry<String,String> entry:
                    optionalHeaders.entrySet()) {
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }
        if (body!=null){
            connection.setDoOutput(true);
            IOUtils.writeStream(connection.getOutputStream(),body);
        }
        Hashtable<String,String> headers=new Hashtable<>();
        if (connection.getDoOutput()){
            connection.getOutputStream().close();
        }
        int respCode=connection.getResponseCode();
        Map<String,List<String>> respHeaders =connection.getHeaderFields();
        if (respHeaders.size()>0){
            for (Map.Entry<String, List<String>> entry :
                    respHeaders.entrySet()) {
                if (entry.getKey()!=null&&entry.getValue()!=null){
                    headers.put(entry.getKey(), entry.getValue().get(0));
                }
            }
        }
        byte[] respBody=IOUtils.getBytesFromStream(connection.getInputStream());
        connection.disconnect();
        connection.getInputStream().close();
        return new HttpResponse(_url,headers,respBody,respCode);
    }
    public HttpResponse doPost(@NotNull String _url, @Nullable Hashtable<String,String> optionalHeaders, @Nullable byte[] body) throws IOException {
        URL url=new URL(_url);
        HttpURLConnection connection= (HttpURLConnection) (url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        if (optionalHeaders!=null){
            for (Map.Entry<String,String> entry:
                    optionalHeaders.entrySet()) {
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }
        if (body!=null){
            connection.setDoOutput(true);
            IOUtils.writeStream(connection.getOutputStream(),body);
        }
        Hashtable<String,String> headers=new Hashtable<>();
        if (connection.getDoOutput()){
            connection.getOutputStream().close();
        }
        int respCode=connection.getResponseCode();
        Map<String,List<String>> respHeaders =connection.getHeaderFields();
        if (respHeaders.size()>0){
            for (Map.Entry<String, List<String>> entry :
                    respHeaders.entrySet()) {
                if (entry.getKey()!=null&&entry.getValue()!=null){
                    headers.put(entry.getKey(), entry.getValue().get(0));
                }
            }
        }
        byte[] respBody=IOUtils.getBytesFromStream(connection.getInputStream());
        connection.disconnect();
        connection.getInputStream().close();
        return new HttpResponse(_url,headers,respBody,respCode);
    }
}
