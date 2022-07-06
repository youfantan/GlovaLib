package glovalib.network.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;

public class TcpPacketServer {
    private int port;
    private boolean flag;
    private Hashtable<Byte,TcpPacketHandler> handlers=new Hashtable<>();
    public TcpPacketServer(int port){
        this.port=port;
        this.flag=true;
    }
    public void start() throws IOException {
        ServerSocket socket=new ServerSocket(port);
        while (flag){
            Socket client=socket.accept();
            client.setKeepAlive(true);
            TcpPacketServerWorker worker=new TcpPacketServerWorker(client,this);
            Thread workerThread=new Thread(worker);
            workerThread.start();
        }
        socket.close();
    }
    public void stop() throws IOException {
        this.flag=false;
        Socket socket=new Socket("localhost",port);
        socket.getOutputStream().write(0xff);
        socket.getOutputStream().flush();
        socket.getOutputStream().close();
        socket.close();
    }
    public void registerPacketHandler(byte packetID,TcpPacketHandler handler) {
        this.handlers.put(packetID,handler);
    }
    TcpPacketHandler getHandler(byte packetID){
        if (handlers.containsKey(packetID)){
            return handlers.get(packetID);
        }
        return null;
    }
}
