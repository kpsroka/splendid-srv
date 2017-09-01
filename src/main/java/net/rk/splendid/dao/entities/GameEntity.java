package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameState;

@Entity
public class GameEntity {
  private @Id String gameRefId;
  private OfyGameConfig gameConfig;
  private OfyGameState gameState;

  private GameEntity() {}

  public GameEntity(GameConfig gameConfig, GameState gameState, String[] playerTokens) {
    this.gameRefId = gameConfig.getRef().getGameId();
    this.gameConfig = OfyGameConfig.fromDto(gameConfig, playerTokens);
    this.gameState = OfyGameState.fromDto(gameState);
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
