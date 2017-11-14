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
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.GameStatus;
import net.rk.splendid.exceptions.PlayerNameEmptyException;
import net.rk.splendid.game.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/g/{gameId}")
public final class RestyGameController {
  static final String REQUEST_PARAMS_ATTRIBUTE_KEY = "requestParams";

  private GameDaoProvider gameDaoProvider;
  private GameActionProvider gameActionProvider;
  private GameJoiner gameJoiner;

  @Inject
  public RestyGameController(
      GameDaoProvider gameDaoProvider,
      GameActionProvider gameActionProvider,
      GameJoiner gameJoiner) {
    this.gameDaoProvider = gameDaoProvider;
    this.gameActionProvider = gameActionProvider;
    this.gameJoiner = gameJoiner;
  }

  @ModelAttribute
  public void handleCommonAttributes(
      Model model,
      @PathVariable("gameId") String gameId,
      @RequestParam(value="playerToken", required=false) String playerToken) {
    model.addAttribute(REQUEST_PARAMS_ATTRIBUTE_KEY, new RequestContext() {
      @Override
      public String getGameRef() {
        return gameId;
      }

      @Override
      public String getPlayerToken() {
        return playerToken;
      }
    });
  }

  @RequestMapping(value="", method=RequestMethod.POST)
  public GameState act(
      @ModelAttribute(REQUEST_PARAMS_ATTRIBUTE_KEY) RequestContext modelParameters,
      @RequestParam("action") String actionType,
      @RequestParam("payload") String payload) {
    GameActionContext context = new GameActionContext(payload, modelParameters.getPlayerToken());
    GameDao gameDao = gameDaoProvider.getGameDao(modelParameters);
    GameEntity gameEntity = gameDao.getGameEntity();
    OfyGameState oldState = gameEntity.getGameState();
    OfyGameState newState = gameActionProvider.getAction(actionType).apply(context, oldState);
    newState.incrementRound();
    gameDao.updateGameState(newState);

    return OfyGameState.toDto(newState, gameEntity.getGameConfig(), modelParameters.getPlayerToken());
  }

  @RequestMapping(value="/join", method=RequestMethod.POST)
  public GameRef join(
      @ModelAttribute(REQUEST_PARAMS_ATTRIBUTE_KEY) RequestContext modelParameters,
      @RequestParam("playerName") String playerName) {
    if (playerName.isEmpty()) {
      throw new PlayerNameEmptyException();
    }

    GameDao gameDao = gameDaoProvider.getGameDao(modelParameters);
    JoinGameParameters parameters = new JoinGameParameters(playerName);
    GameEntity entity = gameDao.getGameEntity();

    String playerToken = gameJoiner.joinGame(entity, parameters);
    gameDao.storeGame(entity);
    return new GameRef(entity.getId(), playerToken);
  }

  @RequestMapping(value="/status", method=RequestMethod.GET)
  public GameStatus getStatus(@ModelAttribute(REQUEST_PARAMS_ATTRIBUTE_KEY) RequestContext modelParameters) {
    return gameDaoProvider.getGameDao(modelParameters).getGameStatus();
  }

  @RequestMapping(value="/config", method=RequestMethod.GET)
  public GameConfig getConfig(@ModelAttribute(REQUEST_PARAMS_ATTRIBUTE_KEY) RequestContext modelParameters) {
    return gameDaoProvider.getGameDao(modelParameters).getGameConfig();
  }

  @RequestMapping(value="/state", method=RequestMethod.GET)
  public GameState getState(
      @ModelAttribute(REQUEST_PARAMS_ATTRIBUTE_KEY) RequestContext modelParameters,
      @RequestParam(value = "lastRound", defaultValue = "-1") int lastRound) {
    GameEntity gameEntity = gameDaoProvider.getGameDao(modelParameters).getGameEntity();

    if (lastRound >= 0 && gameEntity.getGameState().getRound() <= lastRound) {
      return null;
    }

    return OfyGameState.toDto(
        gameEntity.getGameState(),
        gameEntity.getGameConfig(),
        modelParameters.getPlayerToken());
  }
}
