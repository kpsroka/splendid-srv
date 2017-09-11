package net.rk.splendid.dto;

public final class GameState {
  private final String gameStatus;
  private final int round;
  private int currentPlayerIndex;
  private final Board board;
  private final PlayerState[] playerState;

  public GameState(
      String gameStatus,
      int round,
      int currentPlayerIndex,
      Board board,
      PlayerState[] playerState) {
    this.gameStatus = gameStatus;
    this.round = round;
    this.currentPlayerIndex = currentPlayerIndex;
    this.board = board;
    this.playerState = playerState;
  }

  public String getGameStatus() {
    return gameStatus;
  }

  public int getRound() {
    return round;
  }

  public Board getBoard() {
    return board;
  }

  public PlayerState[] getPlayerState() {
    return playerState;
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}
