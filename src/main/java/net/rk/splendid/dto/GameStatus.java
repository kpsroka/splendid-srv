package net.rk.splendid.dto;

public final class GameStatus {
  private final String statusString;

  public GameStatus(String statusString) {
    this.statusString = statusString;
  }

  public String getGameStatus() {
    return statusString;
  }
}
