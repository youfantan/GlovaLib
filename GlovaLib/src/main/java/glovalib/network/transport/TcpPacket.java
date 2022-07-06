package glovalib.network.transport;

public class TcpPacket {
    public TcpPacket(byte packetID, byte[] packet) {
        this.packetID = packetID;
        this.packetLength = packet.length;
        this.packet = packet;
    }

    public byte getPacketID() {
        return packetID;
    }

    public TcpPacket setPacketID(byte packetID) {
        this.packetID = packetID;
        return this;
    }

    public int getPacketLength() {
        return packet.length;
    }

    public byte[] getPacket() {
        return packet;
    }

    public TcpPacket setPacket(byte[] packet) {
        this.packet = packet;
        return this;
    }

    byte packetID;
    int packetLength;
    byte[] packet;

}
