package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.rk.splendid.dto.GameConfig;

@Entity
public class GameEntity {
  @Id String gameRefId;
  OfyGameConfig gameConfig;
  OfyGameState gameState;

  public GameEntity() {}

  public GameEntity(GameConfig gameCfg) {
    this.gameRefId = gameCfg.getRef().getId();
    this.gameConfig = new OfyGameConfig(gameCfg);
  }

  public String getId() {
    return gameRefId;
  }
}
