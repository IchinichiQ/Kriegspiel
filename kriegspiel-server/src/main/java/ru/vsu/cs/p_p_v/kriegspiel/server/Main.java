package ru.vsu.cs.p_p_v.kriegspiel.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter port number as argument");
            return;
        }

        int port = Integer.parseInt(args[0]);

        GameServer server = new GameServer(port);

        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}