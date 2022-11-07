package ru.vsu.cs.p_p_v.game;

public interface GameEventListener {
    default void onNextTurn() {}
    default void onUnitMove() {};
    default void onAttack() {};
}
