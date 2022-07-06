package glovalib.tests;

import glovalib.network.transport.TcpPacket;
import glovalib.network.transport.TcpPacketClient;
import glovalib.network.transport.TcpPacketServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TcpTransPortTest {
    @Test
    public void testTcpPacket() throws IOException, InterruptedException {
        TcpPacketServer server=new TcpPacketServer(9281);
        server.registerPacketHandler((byte) 0x00, (packet) -> {
            System.out.println("---------------- S E R V E R ----------------");
            System.out.println(Integer.toHexString(packet.getPacketID()));
            System.out.println(packet.getPacketLength());
            System.out.println(new String(packet.getPacket(), StandardCharsets.UTF_8));
            return new TcpPacket((byte) 0x00,"Hello Packet Server".getBytes(StandardCharsets.UTF_8));
        });
        new Thread(()->{
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(500);
        TcpPacketClient client=new TcpPacketClient("localhost",9281);
        TcpPacket packet=client.post(new TcpPacket((byte) 0x00,"Hello Packet Client".getBytes(StandardCharsets.UTF_8)));
        System.out.println("---------------- C L I E N T ----------------");
        System.out.println(Integer.toHexString(packet.getPacketID()));
        System.out.println(packet.getPacketLength());
        System.out.println(new String(packet.getPacket(), StandardCharsets.UTF_8));
        client.stop();
        server.stop();
    }
}
