package ru.vsu.cs.p_p_v.kriegspiel.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol.JsonProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private final int port;
    private GameSession lastSession = null;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        System.out.printf("Server started on: %d%n", port);
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.printf("Client connected from: %s%n", socket.getInetAddress());
            if (lastSession == null) {
                lastSession = new GameSession(new JsonProtocol(), socket);
                new Thread(lastSession).start();
            } else {
                lastSession.addSecondPlayer(new JsonProtocol(), socket);
                lastSession = null;
            }
        }
    }
}
