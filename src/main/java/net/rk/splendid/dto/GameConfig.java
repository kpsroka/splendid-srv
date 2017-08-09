package net.rk.splendid.dto;

public final class GameConfig {
  private final GameRef gameRef;

  public GameConfig(GameRef gameRef) {
    this.gameRef = gameRef;
  }

  public GameRef getRef() { return gameRef; }

  public Player[] getPlayers() { return FakeData.FIXED_PLAYERS; }
}
