package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.UnitCombatStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkGame implements Game {
    Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public NetworkGame(String server, int port) {
        Board kek = new Board();

        try {
            socket = new Socket(server, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            //TODO Handle exception properly
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                while (true) {
                    System.out.println(in.read());
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public Teams getCurrentTurnTeam() {
        return null;
    }

    @Override
    public int getLeftMoves() {
        return 0;
    }

    @Override
    public boolean isAttackUsed() {
        return false;
    }

    @Override
    public void endTurn() {

    }

    @Override
    public int getBoardSizeX() {
        return 0;
    }

    @Override
    public int getBoardSizeY() {
        return 0;
    }

    @Override
    public BoardCell getBoardCell(Coordinate cellCoordinate) {
        return null;
    }

    @Override
    public boolean unitCanMove(Coordinate unitCoordinate) {
        return false;
    }

    @Override
    public void moveUnit(Coordinate unitCoordinate, Coordinate destCellCoordinate) {

    }

    @Override
    public boolean unitCanBeCaptured(Coordinate unitCoordinate) {
        return false;
    }

    @Override
    public UnitCombatStats getUnitCombatStats(Coordinate unitCoordinate) {
        return null;
    }

    @Override
    public void attackUnit(Coordinate unitCoordinate) {

    }

    @Override
    public void addGameEventListener(GameEventListener gameListener) {

    }

}
