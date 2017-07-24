package net.rk.splendid.dao;

public final class GameState {
  private final int round;
  private final Board board;
  private final PlayerState[] playerState;

  public GameState(String gameRef) {
    this.round = 0;
    this.board = FakeData.CreateRandomBoard();
    this.playerState =
        FakeData.CreateRandomPlayerState(FakeData.FIXED_PLAYERS.length);
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
}
