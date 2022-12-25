package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

public interface NetworkGameEventListener {
    default void onWaitingPhase() {};
    default void onGamePhase() {};
}
