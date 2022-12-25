package ru.vsu.cs.p_p_v.kriegspiel.server;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.CellConnectionsData;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.CellTypeData;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.UnitData;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.Packet;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.client.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol.Protocol;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitCombatStats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameSession implements Runnable {
    private Client clientNorth = null;
    private Client clientSouth = null;

    Game game;
    private boolean gameOver = false;

    public GameSession(Protocol protocol, Socket socket) {
        try {
            DataInputStream inNorth = new DataInputStream(socket.getInputStream());
            DataOutputStream outNorth = new DataOutputStream(socket.getOutputStream());
            clientNorth = new Client(socket, protocol, inNorth, outNorth);

            sendPacket(new WaitingPhase(), Teams.North);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                while (!gameOver) {
                    int length = clientNorth.in.available();
                    if(length > 0) {
                        byte[] packetLengthBytes = new byte[3];
                        clientNorth.in.read(packetLengthBytes, 0, 3);
                        int packetLength = (packetLengthBytes[0] & 0xFF) << 16 | (packetLengthBytes[1] & 0xFF) << 8 | (packetLengthBytes[2] & 0xFF);

                        byte[] packet = new byte[packetLength];
                        clientNorth.in.readFully(packet, 0, packet.length);

                        handlePacket(protocol.parsePacket(packet), Teams.North);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addSecondPlayer(Protocol protocol, Socket socket) {
        if (clientSouth != null)
            return;

        try {
            DataInputStream inSouth = new DataInputStream(socket.getInputStream());
            DataOutputStream outSouth = new DataOutputStream(socket.getOutputStream());

            clientSouth = new Client(socket, protocol, inSouth, outSouth);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                while (!gameOver) {
                    int length = clientSouth.in.available();
                    if(length > 0) {
                        byte[] packetLengthBytes = new byte[3];
                        clientSouth.in.read(packetLengthBytes, 0, 3);
                        int packetLength = (packetLengthBytes[0] & 0xFF) << 16 | (packetLengthBytes[1] & 0xFF) << 8 | (packetLengthBytes[2] & 0xFF);

                        byte[] packet = new byte[packetLength];
                        clientSouth.in.readFully(packet, 0, packet.length);

                        handlePacket(protocol.parsePacket(packet), Teams.South);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            while (clientSouth == null) {
                Thread.sleep(100);
            }

            game = new LocalGame(Path.of("field.json"), Path.of("units.json"));

            game.addGameEventListener(new GameEventListener() {
                @Override
                public void onNextTurn() {
                    sendPacket(new NextTurn(game.getCurrentTurnTeam()), Teams.Both);
                }

                @Override
                public void onUnitMove(MoveUnitResult result) {
                    if (result == MoveUnitResult.Success)
                        sendBoardUpdatePacket();

                    sendPacket(new UnitMoved(result), Teams.Both);
                }

                @Override
                public void onAttack(AttackUnitResult result) {
                    if (result == AttackUnitResult.Capture)
                        sendBoardUpdatePacket();

                    sendPacket(new UnitAttacked(result), Teams.Both);
                }

                @Override
                public void onWin(Teams winner) {
                    gameOver = true;
                    sendPacket(new EndPhase(winner), Teams.Both);
                }
            });

            sendBoardCreatePacket();
            sendBoardUpdatePacket();

            sendPacket(new GamePhase(Teams.North, game.getCurrentTurnTeam()), Teams.North);
            sendPacket(new GamePhase(Teams.South, game.getCurrentTurnTeam()), Teams.South);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePacket(Packet packet, Teams team) {
        System.out.printf("RECEIVED (%s): %s\n", team, packet.type);

        switch (packet) {
            case AttackUnit attackUnit -> handleAttackUnit(attackUnit, team);
            case MoveUnit moveUnit -> handleMoveUnit(moveUnit, team);
            case EndTurn endTurn -> handleEndTurn(endTurn, team);
            default -> throw new IllegalStateException("Unexpected packet type: " + packet.type);
        }
    }

    private void handleAttackUnit(AttackUnit packet, Teams team) {
        if (team != game.getCurrentTurnTeam()) {
            sendPacket(new UnitAttacked(AttackUnitResult.NotMyTurn), team);
            return;
        }

        game.attackUnit(packet.unitCoordinate);
    }

    private void handleMoveUnit(MoveUnit packet, Teams team) {
        if (team != game.getCurrentTurnTeam()) {
            sendPacket(new UnitMoved(MoveUnitResult.NotMyTurn), Teams.Both);
            return;
        }

        game.moveUnit(packet.moveFrom, packet.moveTo);
    }

    private void handleEndTurn(EndTurn packet, Teams team) {
        if (team != game.getCurrentTurnTeam()) {
            return;
        }

        game.endTurn();
    }

    private void sendPacket(Packet packet, Teams team) {
        try {
            switch (team) {
                case South -> {
                    clientSouth.out.write(clientSouth.protocol.encodePacket(packet));
                    System.out.printf("SENT (South): %s\n", packet.type);
                }
                case North -> {
                    clientNorth.out.write(clientNorth.protocol.encodePacket(packet));
                    System.out.printf("SENT (North): %s\n", packet.type);
                }
                case Both -> {
                    clientSouth.out.write(clientSouth.protocol.encodePacket(packet));
                    System.out.printf("SENT (South): %s\n", packet.type);

                    clientNorth.out.write(clientNorth.protocol.encodePacket(packet));
                    System.out.printf("SENT (North): %s\n", packet.type);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendBoardCreatePacket() {
        List<CellTypeData> cellData = new ArrayList<>();
        for (int x = 0; x < game.getBoardSizeX(); x++) {
            for (int y = 0; y < game.getBoardSizeY(); y++) {
                Coordinate coordinate = new Coordinate(x, y);
                BoardCell cell = game.getBoardCell(coordinate);

                if (cell instanceof Empty)
                    continue;

                String type;
                switch (cell) {
                    case Fortress fortress -> type = "frt";
                    case Mountain mountain -> type = "mnt";
                    case MountainPass mountainPass -> type = "mps";
                    default -> throw new IllegalStateException("Unexpected cell type: " + cell);
                }

                cellData.add(new CellTypeData(coordinate, type));
            }
        }

        sendPacket(new BoardCreated(cellData), Teams.Both);
    }

    private void sendBoardUpdatePacket() {
        List<UnitData> unitData = new ArrayList<>();
        List<CellConnectionsData> cellConnectionsData = new ArrayList<>();

        for (int x = 0; x < game.getBoardSizeX(); x++) {
            for (int y = 0; y < game.getBoardSizeY(); y++) {
                Coordinate coordinate = new Coordinate(x, y);
                BoardCell cell = game.getBoardCell(coordinate);

                CellConnectionsData curConnectionData = new CellConnectionsData(coordinate, cell.hasNorthConnection(), cell.hasSouthConnection(), cell.getNorthConnectionsDirections(),cell.getSouthConnectionsDirections());
                cellConnectionsData.add(curConnectionData);

                BoardUnit unit = cell.getUnit();
                if (unit == null)
                    continue;

                String type;
                switch (unit) {
                    case Arsenal ars -> type = "ars";
                    case Cannon can -> type = "can";
                    case Cavalry cav -> type = "cav";
                    case Infantry inf -> type = "inf";
                    case Relay ars -> type = "rel";
                    case SwiftCannon ars -> type = "swC";
                    case SwiftRelay ars -> type = "swR";
                    default -> throw new IllegalStateException("Unexpected unit type: " + unit);
                }

                boolean canMove = game.unitCanMove(coordinate);
                boolean canBeCaptured = game.unitCanBeCaptured(coordinate);
                UnitCombatStats combatStats = game.getUnitCombatStats(coordinate);

                UnitData curUnitData = new UnitData(type, coordinate, unit.getTeam(), canMove, canBeCaptured, combatStats);
                unitData.add(curUnitData);
            }
        }

        sendPacket(new BoardUpdate(unitData, cellConnectionsData), Teams.Both);
    }
}
