package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.PacketType;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonProtocol implements Protocol {
    @Override
    public Packet parsePacket(byte[] data) {
        String jsonString = new String(data, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        String typeName = (String) gson.fromJson(jsonString, Map.class).get("type");
        PacketType type = PacketType.valueOf(typeName);

        Packet packet = null;
        switch (type) {
            case WaitingPhase -> packet = gson.fromJson(jsonString, WaitingPhase.class);
            case GamePhase -> packet = gson.fromJson(jsonString, GamePhase.class);
            case EndPhase -> packet = gson.fromJson(jsonString, EndPhase.class);
            case OpponentDisconnected -> packet = gson.fromJson(jsonString, OpponentDisconnected.class);
            case MoveUnit -> packet = gson.fromJson(jsonString, MoveUnit.class);
            case UnitMoved -> packet = gson.fromJson(jsonString, UnitMoved.class);
            case AttackUnit -> packet = gson.fromJson(jsonString, AttackUnit.class);
            case UnitAttacked -> packet = gson.fromJson(jsonString, UnitAttacked.class);
            case EndTurn -> packet = gson.fromJson(jsonString, EndTurn.class);
            case BoardUpdate -> packet = gson.fromJson(jsonString, BoardUpdate.class);
            case BoardCreated -> packet = gson.fromJson(jsonString, BoardCreated.class);
        }

        return packet;
    }

    @Override
    public byte[] encodePacket(Packet packet) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(packet);

        return jsonString.getBytes(StandardCharsets.UTF_8);
    }
}
