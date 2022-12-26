package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.client.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets.server.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.protocol.Protocol;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.stats.UnitCombatStats;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkGame implements Game {
    Protocol protocol;
    Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private Teams myTeam = null;
    private Teams currentTurnTeam = null;

    private Board board;

    private int leftMoves = 5;
    private boolean isAttackUsed = false;
    List<UnitData> unitData;

    private List<NetworkGameEventListener> networkGameEventListeners;
    private List<GameEventListener> gameEventListeners;

    public NetworkGame(String server, int port, Protocol protocol) {
        this.protocol = protocol;
        this.networkGameEventListeners = new ArrayList<>();
        this.gameEventListeners = new ArrayList<>();
        this.board = new Board();

        try {
            socket = new Socket(server, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                while (true) {
                    int length = in.available();
                    if(length > 0) {
                        byte[] packetLengthBytes = new byte[3];
                        in.read(packetLengthBytes, 0, 3);
                        int packetLength = (packetLengthBytes[0] & 0xFF) << 16 | (packetLengthBytes[1] & 0xFF) << 8 | (packetLengthBytes[2] & 0xFF);

                        byte[] packet = new byte[packetLength];
                        in.readFully(packet, 0, packet.length);

                        handlePacket(protocol.parsePacket(packet));
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendPacket(Packet packet) {
        try {
            out.write(protocol.encodePacket(packet));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePacket(Packet packet) {
        switch (packet) {
            case WaitingPhase waitingPhase -> handleWaitingPhasePacket(waitingPhase);
            case GamePhase gamePhase -> handleGamePhasePacket(gamePhase);
            case EndPhase endPhase -> handleEndPhasePacket(endPhase);
            case BoardCreated boardCreated -> handleBoardCreatedPacket(boardCreated);
            case BoardUpdate boardUpdate -> handleBoardUpdatedPacket(boardUpdate);
            case NextTurn nextTurn -> handleNextTurnPacket(nextTurn);
            case UnitAttacked unitAttacked -> handleUnitAttackedPacket(unitAttacked);
            case UnitMoved unitMoved -> handleUnitMovedPacket(unitMoved);
            default -> throw new IllegalStateException("Unexpected packet type: " + packet.type);
        }
    }

    private void handleWaitingPhasePacket(WaitingPhase packet) {
        for (NetworkGameEventListener listener : networkGameEventListeners)
            listener.onWaitingPhase();
    }

    private void handleGamePhasePacket(GamePhase packet) {
        myTeam = packet.clientTeam;
        currentTurnTeam = packet.currentTurnTeam;

        for (NetworkGameEventListener listener : networkGameEventListeners)
            listener.onGamePhase();
    }

    private void handleEndPhasePacket(EndPhase endPhase) {
        for (GameEventListener listener : gameEventListeners)
            listener.onWin(endPhase.winner);
    }

    private void handleBoardCreatedPacket(BoardCreated packet) {
        List<CellTypeData> cellTypeData = packet.getCellTypeData();
        board.appendFieldFromCellTypeData(cellTypeData);
    }

    private void handleBoardUpdatedPacket(BoardUpdate packet) {
        List<CellConnectionsData> cellConnectionsData = packet.getCellConnectionsData();
        this.unitData = packet.getUnitData();

        board.removeAllUnits();
        board.appendUnitsFromUnitData(this.unitData);

        board.clearCellConnections(Teams.North);
        board.clearCellConnections(Teams.South);
        board.appendCellConnections(cellConnectionsData);
    }

    private void handleNextTurnPacket(NextTurn nextTurn) {
        leftMoves = 5;
        isAttackUsed = false;
        currentTurnTeam = nextTurn.nextTurnTeam;

        for (GameEventListener listener : gameEventListeners)
            listener.onNextTurn();
    }

    private void handleUnitAttackedPacket(UnitAttacked unitAttacked) {
        if (unitAttacked.result == AttackUnitResult.Capture)
            isAttackUsed = true;

        for (GameEventListener listener : gameEventListeners)
            listener.onAttack(unitAttacked.result);
    }

    private void handleUnitMovedPacket(UnitMoved unitMoved) {
        if (unitMoved.result == MoveUnitResult.Success)
            leftMoves -= 1;

        for (GameEventListener listener : gameEventListeners)
            listener.onUnitMove(unitMoved.result);
    }

    private UnitData getUnitDataByPosition(Coordinate position) {
        return unitData.stream().filter(data -> data.position.equals(position)).findFirst().orElse(null);
    }

    @Override
    public Teams getMyTeam() {
        return myTeam;
    }

    @Override
    public boolean isOnlineGame() {
        return true;
    }

    @Override
    public Teams getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    @Override
    public int getLeftMoves() {
        return leftMoves;
    }

    @Override
    public boolean isAttackUsed() {
        return isAttackUsed;
    }

    @Override
    public void endTurn() {
        sendPacket(new EndTurn());
    }

    @Override
    public int getBoardSizeX() {
        return board.xSize;
    }

    @Override
    public int getBoardSizeY() {
        return board.ySize;
    }

    @Override
    public BoardCell getBoardCell(Coordinate cellCoordinate) {
        return board.getCell(cellCoordinate);
    }

    @Override
    public boolean unitCanMove(Coordinate unitCoordinate) {
        return getUnitDataByPosition(unitCoordinate).canMove;
    }

    @Override
    public void moveUnit(Coordinate unitCoordinate, Coordinate destCellCoordinate) {
        sendPacket(new MoveUnit(unitCoordinate, destCellCoordinate));
    }

    @Override
    public boolean unitCanBeCaptured(Coordinate unitCoordinate) {
        return getUnitDataByPosition(unitCoordinate).canBeCaptured;
    }

    @Override
    public UnitCombatStats getUnitCombatStats(Coordinate unitCoordinate) {
        return getUnitDataByPosition(unitCoordinate).combatStats;
    }

    @Override
    public void attackUnit(Coordinate unitCoordinate) {
        sendPacket(new AttackUnit(unitCoordinate));
    }

    @Override
    public void addGameEventListener(GameEventListener gameListener) {
        gameEventListeners.add(gameListener);
    }

    public void addNetworkGameEventListener(NetworkGameEventListener gameListener) {
        networkGameEventListeners.add(gameListener);
    }

}
