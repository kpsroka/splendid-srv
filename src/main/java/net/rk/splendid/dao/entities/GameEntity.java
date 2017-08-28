package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameState;

@Entity
public class GameEntity {
  @Id String gameRefId;
  OfyGameConfig gameConfig;
  OfyGameState gameState;

  public GameEntity() {}

  public GameEntity(GameConfig gameCfg) {
    this.gameRefId = gameCfg.getRef().getId();
    this.gameConfig = OfyGameConfig.fromDto(gameCfg);
  }

  public GameEntity(GameConfig gameCfg, GameState gameState) {
    this.gameRefId = gameCfg.getRef().getId();
    this.gameConfig = OfyGameConfig.fromDto(gameCfg);
    this.gameState = OfyGameState.fromDto(gameState);
  }

  public String getId() {
    return gameRefId;
  }
  public OfyGameConfig getGameConfig() { return gameConfig; }
  public OfyGameState getGameState() { return gameState; }
}
