package ru.vsu.cs.p_p_v.kriegspiel.client.cli;

import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.BoardUnit;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.UnitBaseStats;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.game.*;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.cell.BoardCell;
import ru.vsu.cs.p_p_v.kriegspiel.sdk.unit.UnitCombatStats;

import java.awt.*;
import java.nio.file.Path;
import java.util.Scanner;

public class CLI {
    Game game;
    public CLI() {
        this.game = new LocalGame(Path.of("field.json"), Path.of("units.json"));;
    }

    public void run() {
        // TODO: Add events in CLI
        //while (game.getWinner() == Teams.None) {
        while (true) {
            printBoard();
            printState();

            while (true) {
                printActions();
                CLIAction selectedAction = getAction();

                boolean shouldBreak = false;
                switch (selectedAction) {
                    case MoveUnit -> actionMoveUnit();
                    case AttackUnit -> actionAttackUnit();
                    case GetUnitStats -> actionGetUnitStats();
                    case EndTurn -> shouldBreak = true;
                }
                if (shouldBreak)
                    break;

                printBoard();
                printState();
            }

            game.endTurn();
        }
    }

    private void actionAttackUnit() {
        Coordinate unitCoordinate = getCoordinateFromUser("Please enter unit X and Y coordinates: ");

        game.attackUnit(unitCoordinate);
        // TODO: Add events to the console
//        AttackUnitResult result = game.attackUnit(unitCoordinate);
//        switch (result) {
//            case NoAttackLeft -> System.out.println("No attacks left on current turn!");
//            case Capture -> System.out.println("The unit was captured");
//            case ForcedRetreat -> System.out.println("The unit was forced to retreat");
//            case None -> System.out.println("Insufficient attack score for a result");
//            case IncorrectUnitCoordinates -> System.out.println("Incorrect unit coordinates!");
//            case UnitInSameTeam -> System.out.println("You can't attack own units!");
//        }
    }

    private void actionGetUnitStats() {
        Coordinate unitCoordinate = getCoordinateFromUser("Please enter unit X and Y coordinates: ");
        BoardUnit unit = game.getBoardCell(unitCoordinate).getUnit();

        if (unit == null) {
            System.out.println("Incorrect unit coordinates!");
            return;
        }

        UnitBaseStats baseStats = unit.getBaseStats();
        int defenseBuff = unit.getDefenseBuff();

        UnitCombatStats combatStats = game.getUnitCombatStats(unitCoordinate);

        System.out.printf("Speed: %d\n", baseStats.speed);
        System.out.printf("Range: %d\n", baseStats.range);
        System.out.printf("Attack: %d\n", baseStats.attack);
        System.out.printf("Defense: %d + %d\n", baseStats.defense, defenseBuff);
        System.out.print('\n');

        System.out.printf("Defense score: %d\n", combatStats.defenseScore);
        System.out.printf("Attack score: %d\n", combatStats.attackScore);
    }

    private void actionMoveUnit() {
        Coordinate unitCoordinate = getCoordinateFromUser("Please enter unit X and Y coordinates: ");
        Coordinate destCellCoordinate = getCoordinateFromUser("Please enter destination cell X and Y coordinates: ");

        game.moveUnit(unitCoordinate, destCellCoordinate);
        // TODO: Add events to the console
//        MoveUnitResult result = game.moveUnit(unitCoordinate, destCellCoordinate);
//        switch (result) {
//            case NoMovementsLeft -> System.out.println("No movements left on current turn!");
//            case UnitAlreadyMoved -> System.out.println("You can move unit only once per turn!");
//            case IncorrectUnitCoordinates -> System.out.println("Incorrect unit coordinates!");
//            case IncorrectCellCoordinates -> System.out.println("Incorrect destination cell coordinates!");
//            case UnitLackSpeed -> System.out.println("The unit doesn't  have enough speed to move into this cell!");
//            case UnitInDifferentTeam -> System.out.println("You can't control units from the enemy team!");
//        }
    }

    private CLIAction getAction() {
        CLIAction selectedAction = null;
        while (selectedAction == null) {
            System.out.print("Please enter action number: ");

            Scanner scan = new Scanner(System.in);
            while (!scan.hasNextInt()) {
                System.out.println("Please enter a number!");
                System.out.print("Please enter action number: ");
                scan.next();
            }
            int enteredNum = scan.nextInt();

            switch (enteredNum) {
                case 1 -> selectedAction = CLIAction.MoveUnit;
                case 2 -> selectedAction = CLIAction.AttackUnit;
                case 3 -> selectedAction = CLIAction.GetUnitStats;
                case 4 -> selectedAction = CLIAction.EndTurn;
            }

            if (selectedAction == null) {
                System.out.println("Please enter valid number!");
            }
        }

        return selectedAction;
    }

    private Coordinate getCoordinateFromUser(String textToDispay) {
        Scanner scan = new Scanner(System.in);

        System.out.print(textToDispay);

        while (!scan.hasNextInt()) {
            System.out.println("Please enter a number!");
            System.out.print("Please enter X coordinate: ");
            scan.next();
        }
        int x = scan.nextInt();

        while (!scan.hasNextInt()) {
            System.out.println("Please enter a number!");
            System.out.print("Please enter Y coordinate: ");
            scan.next();
        }
        int y = scan.nextInt();

        return new Coordinate(--x, --y);
    }

    private void printActions() {
        System.out.println("Actions:");
        System.out.println("1) Move unit");
        System.out.println("2) Attack unit");
        System.out.println("3) Get unit stats");
        System.out.println("4) End turn");
    }

    private void printState() {
        System.out.print("Current turn: ");
        switch (game.getCurrentTurnTeam()) {
            case South -> System.out.print("South");
            case North -> System.out.print("North");
        }
        System.out.print(" | ");

        System.out.printf("Unit movements left : %d | ", game.getLeftMoves());
        System.out.printf("Is attack used: %s ", game.isAttackUsed() ? "yes" : "no");

        System.out.print('\n');
    }

    private void printBoard() {
        // Идея с цветными клетками https://stackoverflow.com/questions/58474570/intellij-console-not-monospaced-in-windows
        int sizeX = game.getBoardSizeX();
        int sizeY = game.getBoardSizeY();

        System.out.print("   ");
        char curColumnCharTop = 'A';
        for (int i = 0; i < sizeX; i++) {
            System.out.print(" " + curColumnCharTop++ + " ");
        }
        System.out.print('\n');

        for (int yIndex = 0; yIndex < sizeY; yIndex++) {
            System.out.printf("%-3d", yIndex + 1);

            for (int xIndex = 0; xIndex < sizeX; xIndex++) {
                BoardCell curCell = game.getBoardCell(new Coordinate(xIndex, yIndex));

                System.out.print(ConsoleColor.ANSI_BLACK);
                String evenCellBackgroundColor;
                String oddCellBackgroundColor;
                if (curCell.getEvenCellColor() != null && curCell.getOddCellColor() != null) {
                    Color evenCellColor = curCell.getEvenCellColor();
                    Color oddCellColor = curCell.getOddCellColor();

                    evenCellBackgroundColor = String.format("\u001B[48;2;%d;%d;%dm", evenCellColor.getRed(), evenCellColor.getGreen(), evenCellColor.getBlue());
                    oddCellBackgroundColor = String.format("\u001B[48;2;%d;%d;%dm", oddCellColor.getRed(), oddCellColor.getGreen(), oddCellColor.getBlue());
                } else {
                    evenCellBackgroundColor = ConsoleColor.ANSI_LIGHT_GRAY_BACKGROUND;
                    oddCellBackgroundColor = ConsoleColor.ANSI_GRAY_BACKGROUND;
                }
                if ((yIndex + xIndex) % 2 == 0) {
                    System.out.print(evenCellBackgroundColor);
                } else {
                    System.out.print(oddCellBackgroundColor);
                }

                if (curCell.getUnit() != null) {
                    BoardUnit curUnit = curCell.getUnit();

                    switch (curUnit.getTeam()) {
                        case North -> System.out.print(ConsoleColor.ANSI_DARK_BLUE);
                        case South -> System.out.print(ConsoleColor.ANSI_DARK_ORANGE);
                    }
                    System.out.print(curUnit.getStringRepresentation());
                } else {
                    if (!curCell.isObstacle()) {
                        if (curCell.hasNorthConnection() || curCell.hasSouthConnection()) {
                            if (curCell.hasNorthConnection() && curCell.hasSouthConnection())
                                System.out.print(ConsoleColor.ANSI_DARK_MAGENTA);
                            else if (curCell.hasNorthConnection())
                                System.out.print(ConsoleColor.ANSI_DARK_BLUE);
                            else
                                System.out.print(ConsoleColor.ANSI_DARK_ORANGE);

                            System.out.print(" * ");
                        } else {
                            System.out.print(curCell.getStringRepresentation());
                        }
                    } else {
                        System.out.print(curCell.getStringRepresentation());
                    }
                }

                System.out.print(ConsoleColor.ANSI_RESET);
            }

            System.out.printf(" %d", yIndex + 1);
            System.out.print('\n');
        }

        System.out.print("   ");
        char curColumnCharBottom = 'A';
        for (int i = 0; i < sizeX; i++) {
            System.out.print(" " + curColumnCharBottom++ + " ");
        }

        System.out.print('\n');
    }
}
