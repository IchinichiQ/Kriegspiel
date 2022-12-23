package ru.vsu.cs.p_p_v.kriegspiel.sdk.game.network.packets;

public enum PacketType {
    WaitingPhase,
    GamePhase,
    EndPhase,
    OpponentDisconnected,
    MoveUnit,
    MoveUnitResult,
    AttackUnit,
    AttackUnitResult,
    EndTurn,
}
