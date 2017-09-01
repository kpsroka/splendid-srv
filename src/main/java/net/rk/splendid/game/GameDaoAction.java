package net.rk.splendid.game;

import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dto.GameRef;

import java.util.function.BiFunction;

public interface GameDaoAction extends BiFunction<GameRef, OfyGameState, OfyGameState> {
}
