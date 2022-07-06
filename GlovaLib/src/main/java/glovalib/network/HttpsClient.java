package glovalib.network;

import glovalib.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HttpsClient {
    public static class Configuration{
        public Configuration(){}
        public boolean isIgnoreCert() {
            return ignoreCert;
        }

        public Configuration setIgnoreCert(boolean ignoreCert) {
            this.ignoreCert = ignoreCert;
            return this;
        }

        public boolean isIgnoreVerifyHostName() {
            return ignoreVerifyHostName;
        }

        public Configuration setIgnoreVerifyHostName(boolean ignoreVerifyHostName) {
            this.ignoreVerifyHostName = ignoreVerifyHostName;
            return this;
        }

        public Configuration(boolean ignoreCert, boolean ignoreVerifyHostName) {
            this.ignoreCert = ignoreCert;
            this.ignoreVerifyHostName = ignoreVerifyHostName;
        }

        private boolean ignoreCert;
        private boolean ignoreVerifyHostName;
    }
    private Configuration configuration;
    public HttpsClient(Configuration configuration){
        this.configuration=configuration;
    }
    public static class HttpsResponse {
        public HttpsResponse(String url, Hashtable<String, String> header, byte[] body, int responseCode) {
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
    private SSLContext getIgnoreCertContext() throws GeneralSecurityException {
        SSLContext context=SSLContext.getInstance("SSL");
        TrustManager manager=new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public X509Certificate[] getAcceptedIssuers() {return null;}
        };
        context.init(null,new TrustManager[]{manager},null);
        return context;
    }
    public HttpsResponse doGet(@NotNull String _url, @Nullable Hashtable<String,String> optionalHeaders, @Nullable byte[] body) throws IOException, GeneralSecurityException {
        URL url=new URL(_url);
        HttpsURLConnection connection= (HttpsURLConnection) (url).openConnection();
        connection.setRequestMethod("GET");
        if (configuration.ignoreVerifyHostName){
            connection.setHostnameVerifier((hostname, session) -> true);
        }
        if (configuration.ignoreCert){
            connection.setSSLSocketFactory(getIgnoreCertContext().getSocketFactory());
        }
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
        return new HttpsResponse(_url,headers,respBody,respCode);
    }
    public HttpsResponse doPost(@NotNull String _url, @Nullable Hashtable<String,String> optionalHeaders, @Nullable byte[] body) throws IOException, GeneralSecurityException {
        URL url=new URL(_url);
        HttpsURLConnection connection= (HttpsURLConnection) (url).openConnection();
        connection.setRequestMethod("POST");
        if (configuration.ignoreVerifyHostName){
            connection.setHostnameVerifier((hostname, session) -> true);
        }
        if (configuration.ignoreCert){
            connection.setSSLSocketFactory(getIgnoreCertContext().getSocketFactory());
        }
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
        return new HttpsResponse(_url,headers,respBody,respCode);
    }
}
