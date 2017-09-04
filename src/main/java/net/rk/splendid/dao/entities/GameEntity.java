package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.UUID;

@Entity
public class GameEntity {
  private @Id String gameRefId;
  private OfyGameConfig gameConfig;
  private OfyGameState gameState;

  private GameEntity() {}

  public GameEntity(OfyGameConfig gameConfig, OfyGameState gameState) {
    this.gameRefId = UUID.randomUUID().toString();
    this.gameConfig = gameConfig;
    this.gameState = gameState;
  }

  public String getId() {
    return gameRefId;
  }
  public OfyGameConfig getGameConfig() {
    return gameConfig;
  }
  public OfyGameState getGameState() {
    return gameState;
  }
  public void setGameState(OfyGameState gameState) {
    this.gameState = gameState;
  }
}
