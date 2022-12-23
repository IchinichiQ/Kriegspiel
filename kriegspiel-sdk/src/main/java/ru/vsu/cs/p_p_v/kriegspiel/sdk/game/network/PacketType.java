package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network;

public enum PacketType {
    WaitingPhase,
    GamePhase,
    EndPhase,
    OpponentDisconnected,
    MoveUnit,
    UnitMoved,
    AttackUnit,
    UnitAttacked,
    EndTurn,
    BoardUpdate,
    BoardCreated
}
