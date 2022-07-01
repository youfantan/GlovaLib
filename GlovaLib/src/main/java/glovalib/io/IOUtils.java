package glovalib.io;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IOUtils {
    public interface stream_read_callback{
        void read(byte[] data,int offset,int length);
    }
    public static void readStream(InputStream stream,stream_read_callback callback) throws IOException {
        BufferedInputStream in=new BufferedInputStream(stream);
        int bytesRead;
        byte[] buffer=new byte[1024];
        while ((bytesRead=in.read(buffer))!=-1){
            callback.read(buffer,0,bytesRead);
        }
    }
    public static void writeStream(OutputStream stream,byte[] content,int offset,int length) throws IOException {
        BufferedOutputStream out=new BufferedOutputStream(stream);
        out.write(content,offset,length);
        out.flush();
    }
    public static void writeStream(OutputStream stream,byte[] content) throws IOException {
        BufferedOutputStream out=new BufferedOutputStream(stream);
        out.write(content);
        out.flush();
    }
    public static byte[] getBytesFromStream(InputStream stream) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        readStream(stream, out::write);
        out.close();
        return out.toByteArray();
    }
    public static String getStringFromStream(InputStream stream) throws IOException {
        return new String(getBytesFromStream(stream), StandardCharsets.UTF_8);
    }
    public static void writeSteamString(OutputStream stream,String content) throws IOException {
        writeStream(stream,content.getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] getBytesFromUrlSafely(String _url) throws IOException {
        URL url=new URL(_url);
        HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
        byte[] bytes=getBytesFromStream(connection.getInputStream());
        connection.getInputStream().close();
        connection.disconnect();
        return bytes;
    }
    public static byte[] getBytesFromUrl(String _url) throws IOException {
        URL url=new URL(_url);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        byte[] bytes=getBytesFromStream(connection.getInputStream());
        connection.getInputStream().close();
        connection.disconnect();
        return bytes;
    }
    public static String getStringFromUrlSafely(String _url) throws IOException {
        return new String(getBytesFromUrlSafely(_url),StandardCharsets.UTF_8);
    }
    public static String getStringFromUrl(String _url) throws IOException{
        return new String(getBytesFromUrl(_url),StandardCharsets.UTF_8);
    }
    public static byte[] gzipCompressBytes(byte[] bytes) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        GZIPOutputStream gOut=new GZIPOutputStream(out);
        gOut.write(bytes);
        gOut.flush();
        gOut.close();
        out.close();
        return out.toByteArray();
    }
    public static byte[] gzipDecompressBytes(byte[] bytes) throws IOException {
        GZIPInputStream in=new GZIPInputStream(new ByteArrayInputStream(bytes));
        byte[] destination=getBytesFromStream(in);
        in.close();
        return destination;
    }
}
