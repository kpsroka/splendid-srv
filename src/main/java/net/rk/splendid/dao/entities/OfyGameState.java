package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.PlayerState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OfyGameState {
  private int round;
  private OfyBoard board;
  private Map<String, OfyPlayerState> playerState = new HashMap<>();

  private OfyGameState() {}

  public static OfyGameState fromDto(GameState gameState) {
    OfyGameState ofyGameState = new OfyGameState();
    ofyGameState.round = gameState.getRound();
    ofyGameState.board = OfyBoard.fromDto(gameState.getBoard());
    ofyGameState.playerState =
        Arrays.stream(gameState.getPlayerState())
            .collect(Collectors.toMap(
                player -> "unset",
                OfyPlayerState::fromDto
            ));

    return ofyGameState;
  }

  public static GameState toDto(OfyGameState ofyGameState) {
    return new GameState(
        ofyGameState.round,
        OfyBoard.toDto(ofyGameState.board),
        ofyGameState.playerState.values().stream()
            .map(OfyPlayerState::toDto)
            .toArray(PlayerState[]::new)
    );
  }

  public OfyBoard getBoard() {
    return board;
  }

  public OfyPlayerState getPlayerState(String playerToken) {
    if (playerState.containsKey(playerToken)) {
      return playerState.get(playerToken);
    } else {
      throw new IllegalArgumentException("No player with token " + playerToken);
    }
  }
}
