package net.rk.splendid.dto;

public final class GameRef {
  private final String gameId;
  private final String playerToken;

  public GameRef(String id) {
    this(id, "unset");
  }

  public GameRef(String gameId, String playerToken) {
    this.gameId = gameId;
    this.playerToken = playerToken;
  }

  public String getId() {
    return this.gameId;
  }

  public String getPlayerToken() {
    return playerToken;
  }
}
