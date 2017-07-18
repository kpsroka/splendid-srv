package net.rk.splendid.dao;

public final class GameConfig {
  private static final Player[] FIXED_PLAYERS = new Player[] {
      new Player("Adam"),
      new Player("Barbara"),
      new Player("Claude"),
      new Player("Dominique")
  };

  private final GameRef gameRef;

  public GameConfig(GameRef gameRef) {
    this.gameRef = gameRef;
  }

  public GameRef getRef() { return gameRef; }

  public Player[] getPlayers() { return FIXED_PLAYERS; }
}
