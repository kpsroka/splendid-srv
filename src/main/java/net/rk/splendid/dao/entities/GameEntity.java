/*
 * Copyright 2017 K. P. Sroka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
