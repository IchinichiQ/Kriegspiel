package ru.vsu.cs.p_p_v.kriegspiel.sdk.game;

import com.google.gson.Gson;
import org.json.simple.parser.ParseException;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.parser.CellJson;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.parser.UnitJson;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private final BoardCell[][] board;
    private final List<BoardUnit> northUnits = new ArrayList<>();
    private final List<BoardUnit> southUnits = new ArrayList<>();

    public final int ySize = 20;
    public final int xSize = 25;

    public Board() {
        board = new BoardCell[ySize][xSize];
        for (int yIndex = 0; yIndex < ySize; yIndex++) {
            for (int xIndex = 0; xIndex < xSize; xIndex++) {
                board[yIndex][xIndex] = new Empty(new Coordinate(xIndex, yIndex));
            }
        }
    }

    public void appendFieldFromFile(Path path) throws IOException, ParseException {
        String jsonString = Files.readString(path);

        appendFieldFromJson(jsonString);
    }

    public void appendFieldFromJson(String jsonString) {
        CellJson[] cells = new Gson().fromJson(jsonString, CellJson[].class);

        for (CellJson cellJson : cells) {
            int x = cellJson.getX();
            int y = cellJson.getY();
            x--;
            y--;

            BoardCell curCell = null;
            switch (cellJson.getType()) {
                case "empty" -> curCell = new Empty(new Coordinate(x, y));
                case "fortress" -> curCell = new Fortress(new Coordinate(x, y));
                case "mountain" -> curCell = new Mountain(new Coordinate(x, y));
                case "mountainPass" -> curCell = new MountainPass(new Coordinate(x, y));
            }

            board[y][x] = curCell;
        }
    }

    public void appendUnitsFromFile(Path path) throws IOException {
        String jsonString = Files.readString(path);

        appendUnitsFromJson(jsonString);
    }

    public void appendUnitsFromJson(String jsonString) {
        UnitJson[] units = new Gson().fromJson(jsonString, UnitJson[].class);

        for (UnitJson unitJson : units) {
            int x = unitJson.getX();
            int y = unitJson.getY();
            x--;
            y--;
            Teams team = null;
            switch (unitJson.getTeam()) {
                case "north" -> team = Teams.North;
                case "south" -> team = Teams.South;
            }

            BoardUnit unit = null;
            switch (unitJson.getType()) {
                case "arsenal" -> unit = new Arsenal(this, team, new Coordinate(x, y));
                case "cannon" -> unit = new Cannon(this, team, new Coordinate(x, y));
                case "cavalry" -> unit = new Cavalry(this, team, new Coordinate(x, y));
                case "infantry" -> unit = new Infantry(this, team, new Coordinate(x, y));
                case "relay" -> unit = new Relay(this, team, new Coordinate(x, y));
                case "swiftCannon" -> unit = new SwiftCannon(this, team, new Coordinate(x, y));
                case "swiftRelay" -> unit = new SwiftRelay(this, team, new Coordinate(x, y));
            }
        }
    }

    public BoardCell getCell(Coordinate cellCoordinate) {
        if (cellCoordinate.x < 0 || cellCoordinate.x > xSize - 1 || cellCoordinate.y < 0 || cellCoordinate.y > ySize - 1) {
            return null;
        }

        return board[cellCoordinate.y][cellCoordinate.x];
    }

    public List<BoardUnit> getNorthUnits() {
        return Collections.unmodifiableList(northUnits);
    }

    public List<BoardUnit> getNorthArsenalUnits() {
        return northUnits.stream().filter(unit -> unit instanceof ArsenalUnit).collect(Collectors.toList());
    }

    public List<BoardUnit> getNorthRelayUnits() {
        return northUnits.stream().filter(unit -> unit instanceof RelayUnit).collect(Collectors.toList());
    }

    public boolean addNorthUnit(BoardUnit boardUnit) {
        return northUnits.add(boardUnit);
    }

    public List<BoardUnit> getSouthUnits() {
        return Collections.unmodifiableList(southUnits);
    }

    public List<BoardUnit> getSouthArsenalUnits() {
        return southUnits.stream().filter(unit -> unit instanceof ArsenalUnit).collect(Collectors.toList());
    }

    public List<BoardUnit> getSouthRelayUnits() {
        return southUnits.stream().filter(unit -> unit instanceof RelayUnit).collect(Collectors.toList());
    }

    public boolean addSouthUnit(BoardUnit boardUnit) {
        return southUnits.add(boardUnit);
    }

    public void removeUnit(BoardUnit boardUnit) {
        getCell(boardUnit.getPosition()).setUnit(null);

        if (boardUnit.getTeam() == Teams.North) {
            northUnits.remove(boardUnit);
        } else {
            southUnits.remove(boardUnit);
        }
    }
}
