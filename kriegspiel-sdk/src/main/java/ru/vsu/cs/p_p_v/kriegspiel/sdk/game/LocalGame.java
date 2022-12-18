package ru.vsu.cs.p_p_v.kriegspiel.sdk.game;

import java.nio.file.Path;

public class LocalGame extends AbstractGame {
    public LocalGame(Path fieldJsonPath, Path unitsJsonPath) {
        super(fieldJsonPath, unitsJsonPath);
    }
}
