package net.rk.splendid.game;

import net.rk.splendid.dto.GameState;

import java.util.function.Function;

public interface GameAction extends Function<GameState, GameState> {
}
