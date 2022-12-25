package ru.vsu.cs.p_p_v.kriegspiel.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
    public final Socket socket;
    public final Protocol protocol;
    public final DataInputStream in;
    public final DataOutputStream out;

    public Client(Socket socket, Protocol protocol, DataInputStream in, DataOutputStream out) {
        this.socket = socket;
        this.protocol = protocol;
        this.in = in;
        this.out = out;
    }
}
