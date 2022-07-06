package glovalib.network.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class TcpPacketServerWorker implements Runnable{
    private Socket client;
    private TcpPacketServer instance;
    TcpPacketServerWorker(Socket client,TcpPacketServer instance){
        this.client=client;
        this.instance=instance;
    }
    @Override
    public void run() {
        try {
            DataInputStream in=new DataInputStream(client.getInputStream());
            DataOutputStream out=new DataOutputStream(client.getOutputStream());
            byte packetID;
            while ((packetID=in.readByte())!=(byte) 0xff){
                TcpPacketHandler handler;
                if ((handler= instance.getHandler(packetID))!=null){
                    int packetLength=in.readInt();
                    byte[] packetContent=in.readNBytes(packetLength);
                    TcpPacket receipt=handler.execute(new TcpPacket(packetID,packetContent));
                    out.writeByte(receipt.getPacketID());
                    out.writeInt(receipt.getPacketLength());
                    out.write(receipt.getPacket());
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
