package glovalib.network.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpPacketClient {
    private String host;
    private int port;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    public TcpPacketClient(String host,int port) throws IOException {
        this.host=host;
        this.port=port;
        Socket socket=new Socket(host,port);
        socket.setKeepAlive(true);
        this.in=new DataInputStream(socket.getInputStream());
        this.out=new DataOutputStream(socket.getOutputStream());
        this.socket=socket;
    }
    public TcpPacket post(TcpPacket packet) throws IOException {
        out.writeByte(packet.getPacketID());
        out.writeInt(packet.getPacketLength());
        out.write(packet.getPacket());
        out.flush();
        byte respPacketID=in.readByte();
        int respPacketLength=in.readInt();
        byte[] respPacketContent=in.readNBytes(respPacketLength);
        return new TcpPacket(respPacketID,respPacketContent);
    }
    public void stop() throws IOException {
        out.writeByte(0xff);
        out.flush();
        out.close();
        in.close();
        socket.close();
    }
}
