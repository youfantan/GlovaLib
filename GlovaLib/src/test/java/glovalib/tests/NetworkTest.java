package glovalib.tests;

import glovalib.network.HttpClient;
import glovalib.network.HttpsClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Hashtable;
import java.util.Map;

public class NetworkTest {
    @Test
    public void testDoHttpGet() throws IOException {
        HttpClient client=new HttpClient();
        Hashtable<String,String> headers=new Hashtable<>();
        headers.put("X-Test-App-Header","Test Header");
        headers.put("User-Agent","GlovaLib HttpsClient");
        HttpClient.HttpResponse response=client.doGet("http://httpbin.org/get",headers,null);
        System.out.println(response.getResponseCode());
        for (Map.Entry<String, String> entry :
                response.getHeader().entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
        System.out.println(response.getUrl());
    }
    @Test
    public void testDoHttpPost() throws IOException {
        HttpClient client=new HttpClient();
        Hashtable<String,String> headers=new Hashtable<>();
        headers.put("X-Test-App-Header","Test Header");
        headers.put("User-Agent","GlovaLib HttpsClient");
        HttpClient.HttpResponse response=client.doPost("http://httpbin.org/post",headers,"Test Body".getBytes(StandardCharsets.UTF_8));
        System.out.println(response.getResponseCode());
        for (Map.Entry<String, String> entry :
                response.getHeader().entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
        System.out.println(response.getUrl());
    }
    @Test
    public void testDoHttpsGet() throws GeneralSecurityException, IOException {
        HttpsClient client=new HttpsClient(new HttpsClient.Configuration().setIgnoreCert(true).setIgnoreVerifyHostName(true));
        Hashtable<String,String> headers=new Hashtable<>();
        headers.put("X-Test-App-Header","Test Header");
        headers.put("User-Agent","GlovaLib HttpsClient");
        HttpsClient.HttpsResponse response=client.doGet("https://httpbin.org/get",headers,null);
        System.out.println(response.getResponseCode());
        for (Map.Entry<String, String> entry :
                response.getHeader().entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
        System.out.println(response.getUrl());
    }
    @Test
    public void testDoHttpsPost() throws GeneralSecurityException, IOException {
        HttpsClient client=new HttpsClient(new HttpsClient.Configuration().setIgnoreCert(true).setIgnoreVerifyHostName(true));
        Hashtable<String,String> headers=new Hashtable<>();
        headers.put("X-Test-App-Header","Test Header");
        headers.put("User-Agent","GlovaLib HttpsClient");
        HttpsClient.HttpsResponse response=client.doPost("https://httpbin.org/post",headers,"Test Body".getBytes(StandardCharsets.UTF_8));
        System.out.println(response.getResponseCode());
        for (Map.Entry<String, String> entry :
                response.getHeader().entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println(new String(response.getBody(), StandardCharsets.UTF_8));
        System.out.println(response.getUrl());
    }
}
