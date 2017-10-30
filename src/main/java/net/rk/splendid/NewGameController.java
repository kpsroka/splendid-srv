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
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.exceptions.PlayerCountOutOfRangeException;
import net.rk.splendid.exceptions.PlayerNameEmptyException;
import net.rk.splendid.game.GameFactory;
import net.rk.splendid.game.GameJoiner;
import net.rk.splendid.game.JoinGameParameters;
import net.rk.splendid.game.NewGameOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public final class NewGameController {
  private final GameFactory gameFactory;
  private final GameDao gameDao;
  private GameJoiner gameJoiner;
  private final CommonSessionParameters sessionParameters;

  @Inject
  public NewGameController(
      GameFactory gameFactory,
      GameDao gameDao,
      GameJoiner gameJoiner,
      CommonSessionParameters sessionParameters) {
    this.gameFactory = gameFactory;
    this.gameDao = gameDao;
    this.gameJoiner = gameJoiner;
    this.sessionParameters = sessionParameters;
  }

  @RequestMapping("/new")
  public GameRef newGame(
      @RequestParam("playerName") String playerName,
      @RequestParam("playerCount") int playerCount) {
    if (playerName.isEmpty()) {
      throw new PlayerNameEmptyException();
    }

    if (playerCount < 2 || playerCount > 5) {
      throw new PlayerCountOutOfRangeException(playerCount);
    }

    NewGameOptions options = new NewGameOptions(playerCount);
    GameEntity gameEntity = gameFactory.apply(options);
    String playerToken = gameJoiner.joinGame(gameEntity, new JoinGameParameters(playerName));

    sessionParameters.setGameRef(gameEntity.getId());
    gameDao.storeGame(gameEntity);
    return new GameRef(gameEntity.getId(), playerToken);
  }
}
