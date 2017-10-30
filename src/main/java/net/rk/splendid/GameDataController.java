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

package net.rk.splendid;

import net.rk.splendid.dao.GameDao;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.GameStatus;
import net.rk.splendid.exceptions.PlayerNameEmptyException;
import net.rk.splendid.game.GameActionContext;
import net.rk.splendid.game.GameActionProvider;
import net.rk.splendid.game.GameJoiner;
import net.rk.splendid.game.JoinGameParameters;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/game")
public final class GameDataController {
  private final GameDao gameDao;
  private GameJoiner gameJoiner;
  private final CommonSessionParameters commonSessionParameters;
  private final GameActionProvider gameActionProvider;

  @Inject
  public GameDataController(
      GameDao gameDao,
      GameJoiner gameJoiner,
      CommonSessionParameters commonSessionParameters,
      GameActionProvider gameActionProvider) {
    this.gameDao = gameDao;
    this.gameJoiner = gameJoiner;
    this.commonSessionParameters = commonSessionParameters;
    this.gameActionProvider = gameActionProvider;
  }

  @ModelAttribute
  public void handleCommonAttributes(
      @RequestParam("id") String gameRefId,
      @RequestParam(value = "playerToken", required = false) String playerToken) {
    commonSessionParameters.setGameRef(gameRefId);
    commonSessionParameters.setPlayerToken(playerToken);
  }

  @RequestMapping("/getConfig")
  public GameConfig getGameConfig() {
    return gameDao.getGameConfig();
  }

  @RequestMapping("/getState")
  public GameState getGameState(
      @RequestParam(value = "lastRound", defaultValue = "-1") int lastRound) {
    GameEntity gameEntity = gameDao.getGameEntity();

    if (lastRound >= 0 && gameEntity.getGameState().getRound() <= lastRound) {
      return null;
    }

    return OfyGameState.toDto(
        gameEntity.getGameState(),
        gameEntity.getGameConfig(),
        commonSessionParameters.getPlayerToken());
  }

  @RequestMapping("/act")
  public GameState executePlayerAction(
      @RequestParam("action") String actionType,
      @RequestParam("payload") String payload) {
    GameActionContext context =
        new GameActionContext(payload, commonSessionParameters.getPlayerToken());
    GameEntity gameEntity = gameDao.getGameEntity();
    OfyGameState oldState = gameEntity.getGameState();
    OfyGameState newState = gameActionProvider.getAction(actionType).apply(context, oldState);
    newState.incrementRound();
    gameDao.updateGameState(newState);
    return OfyGameState.toDto(
        newState,
        gameEntity.getGameConfig(),
        commonSessionParameters.getPlayerToken());
  }

  @RequestMapping("/join")
  public GameConfig joinGame(@RequestParam("playerName") String playerName) {
    if (playerName.isEmpty()) {
      throw new PlayerNameEmptyException();
    }

    JoinGameParameters parameters = new JoinGameParameters(playerName);
    GameEntity entity = gameDao.getGameEntity();
    commonSessionParameters.setPlayerToken(gameJoiner.joinGame(entity, parameters));
    gameDao.storeGame(entity);

    return gameDao.getGameConfig();
  }

  @RequestMapping("/getStatus")
  public GameStatus getGameStatus() {
    return gameDao.getGameStatus();
  }
}
