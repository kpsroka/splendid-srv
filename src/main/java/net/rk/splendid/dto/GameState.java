package net.rk.splendid.dto;

import java.util.Arrays;

public final class GameState {
  private final int round;
  private final Board board;
  private final PlayerState[] playerState;

  public GameState() {
    this(
        0,
        FakeData.CreateRandomBoard(),
        FakeData.CreateRandomPlayerState(FakeData.FIXED_PLAYERS.length));
  }

  public GameState(int round, Board board, PlayerState[] playerState) {
    this.round = round;
    this.board = board;
    this.playerState = playerState;
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

  public GameState createDeepCopy() {
    PlayerState[] newPlayerState = Arrays.stream(playerState)
        .map(PlayerState::createDeepCopy)
        .toArray(PlayerState[]::new);

    return new GameState(this.round, this.board.createDeepCopy(), newPlayerState);
  }
}
