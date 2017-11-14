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

package net.rk.splendid.dao;

import com.googlecode.objectify.Key;
import net.rk.splendid.RequestContext;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameConfig;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameStatus;
import net.rk.splendid.exceptions.GameNotFoundException;

import javax.inject.Inject;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Named
public final class GameDaoImpl implements GameDao {
  private final RequestContext requestContext;

  @Inject
  public GameDaoImpl(RequestContext requestContext) {
    this.requestContext = requestContext;
  }

  @Override
  public void storeGame(GameEntity gameEntity) {
    ofy().save().entity(gameEntity).now();
  }

  @Override
  public GameConfig getGameConfig() {
    String gameRefId = requestContext.getGameRef();
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    return OfyGameConfig.toDto(gameRefId, requestContext.getPlayerToken(), gameEntity.getGameConfig());
  }

  private OfyGameState getGameState() {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, requestContext.getGameRef());
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();

    if (gameEntity == null) {
      throw new GameNotFoundException();
    }

    return gameEntity.getGameState();
  }

  @Override
  public void updateGameState(OfyGameState newState) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, this.requestContext.getGameRef());
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    gameEntity.setGameState(newState);
    ofy().save().entity(gameEntity);
  }

  @Override
  public GameEntity getGameEntity() {
    Key<GameEntity> gameRefKey =
        Key.create(GameEntity.class, requestContext.getGameRef());
    GameEntity entity = ofy().load().key(gameRefKey).now();
    if (entity != null) {
      return entity;
    } else {
      throw new GameNotFoundException();
    }
  }

  @Override
  public GameStatus getGameStatus() {
    return new GameStatus(getGameState().getGameStatus().name());
  }
}
