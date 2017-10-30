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

package net.rk.splendid.game;

import net.rk.splendid.dao.entities.*;
import net.rk.splendid.exceptions.AllPlayersJoinedException;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GameJoinerImplTest {
  private final String[] playerRefs = new String[] {"Foo", "Bar", "Baz"};
  private OfyGameConfig gameConfig;
  private OfyGameState gameState;

  @Before
  public void setUp() {
    gameConfig = OfyGameConfig.create(
        new OfyPlayer[] {
            OfyPlayer.create(0, "A"),
            OfyPlayer.create(1, "B"),
            OfyPlayer.create(2, "C")
        },
        playerRefs
    );

    gameState = OfyGameState.create(
        playerRefs,
        integer -> new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList())));
  }

  @Test
  public void throwsExceptionIfAllPlayersJoinedAlready() {
    for (String playerRef : playerRefs) {
      gameConfig.setPlayerJoined(playerRef);
    }

    GameEntity entity = new GameEntity(gameConfig, gameState);
    GameJoinerImpl gameJoiner = new GameJoinerImpl();

    try {
      gameJoiner.joinGame(entity, new JoinGameParameters("D"));
      Assert.fail("Should have thrown AllPlayersJoinedException");
    } catch (AllPlayersJoinedException e) {
      // pass!
    }
  }

  @Test
  public void marksFirstAvailablePlayerAsJoined() {
    gameConfig.setPlayerJoined(playerRefs[0]);

    GameEntity entity = new GameEntity(gameConfig, gameState);
    GameJoinerImpl gameJoiner = new GameJoinerImpl();

    gameJoiner.joinGame(entity, new JoinGameParameters("D"));
    Assert.assertTrue(entity.getGameConfig().getPlayersOrdered().get(playerRefs[1]).hasJoined());
    Assert.assertFalse(entity.getGameConfig().getPlayersOrdered().get(playerRefs[2]).hasJoined());
  }

  @Test
  public void renamesJoinedPlayer() {
    gameConfig.setPlayerJoined(playerRefs[0]);

    GameEntity entity = new GameEntity(gameConfig, gameState);
    GameJoinerImpl gameJoiner = new GameJoinerImpl();
    String playerName = "new player";
    JoinGameParameters joinGameParameters = new JoinGameParameters(playerName);

    gameJoiner.joinGame(entity, joinGameParameters);
    Assert.assertEquals(playerName, entity.getGameConfig().getPlayersOrdered().get(playerRefs[1]).getName());
  }

  @Test
  public void returnsJoinedPlayerRef() {
    gameConfig.setPlayerJoined(playerRefs[0]);

    GameEntity entity = new GameEntity(gameConfig, gameState);
    GameJoinerImpl gameJoiner = new GameJoinerImpl();

    String returnedPlayerRef = gameJoiner.joinGame(entity, new JoinGameParameters("D"));
    Assert.assertEquals(playerRefs[1], returnedPlayerRef);
  }

  @Test
  public void setsGameStatusToUnderwayOnceLastPlayerJoins() {
    gameConfig.setPlayerJoined(playerRefs[0]);

    GameEntity entity = new GameEntity(gameConfig, gameState);
    GameJoinerImpl gameJoiner = new GameJoinerImpl();

    gameJoiner.joinGame(entity, new JoinGameParameters("D"));
    Assert.assertNotEquals(OfyGameStatus.UNDERWAY, entity.getGameState().getGameStatus());

    gameJoiner.joinGame(entity, new JoinGameParameters("E"));
    Assert.assertEquals(OfyGameStatus.UNDERWAY, entity.getGameState().getGameStatus());
  }
}
