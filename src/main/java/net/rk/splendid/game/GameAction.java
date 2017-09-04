package net.rk.splendid.game;

import net.rk.splendid.dao.entities.OfyGameState;

import java.util.function.BiFunction;

public interface GameAction extends BiFunction<String, OfyGameState, OfyGameState> {
}
