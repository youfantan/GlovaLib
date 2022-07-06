package glovalib.network.transport;

public interface TcpPacketHandler {
    TcpPacket execute(TcpPacket packet);
}
