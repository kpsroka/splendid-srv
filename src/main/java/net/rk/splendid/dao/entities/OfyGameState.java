package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.PlayerState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OfyGameState {
  private OfyGameStatus gameStatus;
  private int round;
  private OfyBoard board;
  private Map<String, OfyPlayerState> playerState = new HashMap<>();

  private OfyGameState() {}

  public static GameState toDto(
      OfyGameState ofyGameState,
      OfyGameConfig ofyGameConfig,
      String playerToken) {
    List<String> playerTokensOrdered = ofyGameConfig.getPlayerTokensOrdered(playerToken);

    return new GameState(
        ofyGameState.gameStatus.name(),
        ofyGameState.round,
        getCurrentPlayerIndex(ofyGameState, ofyGameConfig.getPlayer(playerToken).getIndex()),
        OfyBoard.toDto(ofyGameState.board),
        playerTokensOrdered.stream()
            .map(ofyGameState.playerState::get)
            .map(OfyPlayerState::toDto)
            .toArray(PlayerState[]::new));
  }

  private static int getCurrentPlayerIndex(OfyGameState gameState, int currentPlayerIndex) {
    int playerCount = gameState.playerState.size();
    return (playerCount + gameState.round - currentPlayerIndex) % playerCount;
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

  public OfyGameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameStatus(OfyGameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public void incrementRound() {
    round++;
  }

  public static OfyGameState create(String[] playerRefs) {
    OfyGameState gameState = new OfyGameState();
    gameState.gameStatus = OfyGameStatus.UNKNOWN;
    gameState.round = 0;
    gameState.board = OfyBoard.create();
    gameState.playerState =
        Arrays.stream(playerRefs)
            .collect(Collectors.toMap(Function.identity(), ref -> OfyPlayerState.create()));
    return gameState;
  }

  public int getRound() {
    return round;
  }
}
