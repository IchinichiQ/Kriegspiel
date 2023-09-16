package ru.vsu.cs.p_p_v.kriegspiel.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static String getTime() {
        String pattern = "hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(new Date());
    }

    public static void logReceivedPacket(Client client, Packet packet) {
        System.out.printf("[%s] %s - RECEIVED (%s): %s\n", getTime(), client.socket.getInetAddress(), client.team, packet.type);
    }

    public static void logSentPacket(Client client, Packet packet) {
        System.out.printf("[%s] %s - SENT (%s): %s\n", getTime(), client.socket.getInetAddress(), client.team, packet.type);
    }

    public static void log(String str) {
        System.out.printf("[%s] %s", getTime(), str);
    }
}
