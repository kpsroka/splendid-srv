package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.PlayerState;

import java.util.Arrays;

public class OfyGameState {
  int round;
  OfyBoard board;
  OfyPlayerState[] playerState;

  private OfyGameState() {}

  static OfyGameState fromDto(GameState gameState) {
    OfyGameState ofyGameState = new OfyGameState();
    ofyGameState.round = gameState.getRound();
    ofyGameState.board = OfyBoard.fromDto(gameState.getBoard());
    ofyGameState.playerState =
        Arrays.stream(gameState.getPlayerState())
            .map(OfyPlayerState::fromDto)
            .toArray(OfyPlayerState[]::new);

    return ofyGameState;
  }

  public static GameState toDto(OfyGameState ofyGameState) {
    return new GameState(
        ofyGameState.round,
        OfyBoard.toDto(ofyGameState.board),
        Arrays.stream(ofyGameState.playerState)
            .map(OfyPlayerState::toDto)
            .toArray(PlayerState[]::new)
    );
  }
}
