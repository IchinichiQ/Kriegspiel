package ru.vsu.cs.p_p_v.kriegspiel.sdk.game;

public interface GameEventListener {
    default void onNextTurn() {}
    default void onUnitMove() {};
    default void onAttack() {};
    default void onWin() {};
}
