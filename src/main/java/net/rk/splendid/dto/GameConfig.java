package net.rk.splendid.dto;

public final class GameConfig {
  private final GameRef gameRef;
  private final Player[] players;

  public GameConfig(GameRef gameRef, Player[] players) {
    this.gameRef = gameRef;
    this.players = players;
  }

  public GameRef getRef() { return gameRef; }

  public Player[] getPlayers() { return players; }
}
