package ru.vsu.cs.p_p_v.game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.vsu.cs.p_p_v.cell.*;
import ru.vsu.cs.p_p_v.unit.*;

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

    public void AppendFieldFromFile(String path) throws IOException, ParseException {
        String jsonString = Files.readString(Path.of(path));

        JSONObject json = (JSONObject) new JSONParser().parse(jsonString);
        JSONArray cells = (JSONArray) json.get("cells");
        for (Object obj : cells) {
            JSONObject cell = (JSONObject) obj;
            int x = (int) (long) cell.get("x");
            int y = (int) (long) cell.get("y");
            x--;
            y--;

            BoardCell curCell = null;
            switch ((String) cell.get("type")) {
                case "empty" -> curCell = new Empty(new Coordinate(x, y));
                case "fortress" -> curCell = new Fortress(new Coordinate(x, y));
                case "mountain" -> curCell = new Mountain(new Coordinate(x, y));
                case "mountainPass" -> curCell = new MountainPass(new Coordinate(x, y));
            }

            board[y][x] = curCell;
        }
    }

    public void AppendUnitsFromFile(String path) throws IOException, ParseException {
        String jsonString = Files.readString(Path.of(path));

        JSONObject json = (JSONObject) new JSONParser().parse(jsonString);
        JSONArray units = (JSONArray) json.get("units");
        for (Object obj : units) {
            JSONObject cell = (JSONObject) obj;
            int x = (int) (long) cell.get("x");
            int y = (int) (long) cell.get("y");
            x--;
            y--;
            Teams team = null;
            switch ((String) cell.get("team")) {
                case "north" -> team = Teams.North;
                case "south" -> team = Teams.South;
            }

            BoardUnit unit = null;
            switch ((String) cell.get("type")) {
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
