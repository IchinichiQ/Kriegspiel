package ru.vsu.cs.p_p_v.kriegspiel.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.Teams;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    public final Teams team;
    public final Socket socket;
    public final Protocol protocol;
    public final DataInputStream in;
    private final DataOutputStream out;

    public Client(Socket socket, Teams team, Protocol protocol, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.team = team;
        this.protocol = protocol;
        this.in = in;
        this.out = out;
    }

    public void sendPacket(Packet packet) {
        try {
            byte[] encodedPacket = this.protocol.encodePacket(packet);
            this.out.write(encodedPacket);
            Logger.logSentPacket(this, packet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
