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
import net.rk.splendid.dao.entities.*;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.exceptions.PlayerCountOutOfRangeException;
import net.rk.splendid.exceptions.PlayerNameEmptyException;
import net.rk.splendid.game.FactoryGenerator;
import net.rk.splendid.game.GameDaoProvider;
import net.rk.splendid.game.GameFactory;
import net.rk.splendid.game.GameJoiner;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.inject.Provider;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class NewGameControllerTest {
  @Mock private GameDao gameDao;
  @Mock private GameDaoProvider gameDaoProviderInterface;
  @Mock private Provider<GameDao> gameDaoProvider;
  @Mock private GameFactory gameFactory;
  @Mock private GameJoiner gameJoiner;

  private final FactoryGenerator factoryGeneratorMock = mock(FactoryGenerator.class);
  private final OfyGameConfig defaultGameConfig =
      OfyGameConfig.create(
          new OfyPlayer[] { OfyPlayer.create(0, "FOO"), OfyPlayer.create(1, "BAR") },
          new String[] { "PlayerOneRef", "PlayerTwoRef" });

  private final OfyGameState defaultGameState =
      OfyGameState.create(
          new String[] { "PlayerOneRef", "PlayerTwoRef" },
          factoryGeneratorMock);

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);

    FactoryGenerator factoryGeneratorMock = mock(FactoryGenerator.class);
    when(factoryGeneratorMock.apply(any()))
        .thenReturn(new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList())));

    when(gameFactory.apply(any()))
        .thenReturn(new GameEntity(defaultGameConfig, defaultGameState));

    when(gameJoiner.joinGame(any(), any()))
        .thenReturn("DefaultPlayerRef");

    when(gameDaoProvider.get()).thenReturn(gameDao);
    when(gameDaoProviderInterface.getGameDao(any())).thenReturn(gameDao);
  }

  @Test
  public void throwsExceptionOnOnePlayerGameRequested() {
    NewGameController controller =
        new NewGameController(gameFactory, gameDaoProviderInterface, gameJoiner);

    try {
      controller.newGame("Player", 1);
      Assert.fail("Expected PlayerCountOutOfRangeException to be thrown.");
    } catch (PlayerCountOutOfRangeException expected) {
      Mockito.verifyZeroInteractions(gameFactory, gameJoiner, gameDaoProvider);
    }
  }

  @Test
  public void throwsExceptionOnTooManyPlayersRequested() {
    NewGameController controller =
        new NewGameController(gameFactory, gameDaoProviderInterface, gameJoiner);

    try {
      controller.newGame("Player", NewGameController.MAX_PLAYERS_ALLOWED + 1);
      Assert.fail("Expected PlayerCountOutOfRangeException to be thrown.");
    } catch (PlayerCountOutOfRangeException expected) {
      Mockito.verifyZeroInteractions(gameFactory, gameJoiner, gameDaoProvider);
    }
  }

  @Test
  public void throwsExceptionOnEmptyPlayerName() {
    NewGameController controller =
        new NewGameController(gameFactory, gameDaoProviderInterface, gameJoiner);

    try {
      controller.newGame("", 2);
      Assert.fail("Expected PlayerNameEmptyException to be thrown.");
    } catch (PlayerNameEmptyException expected) {
      Mockito.verifyZeroInteractions(gameFactory, gameJoiner, gameDaoProvider);
    }
  }

  @Test
  public void returnsGameRefWithProperIds() {
    // new instances to generate unique, non-shared IDs for the test.

    GameEntity newGameEntity = new GameEntity(defaultGameConfig, defaultGameState);
    when(gameFactory.apply(any())).thenReturn(newGameEntity);

    String expectedPlayerRef = "ExpectedPlayer1Ref";
    String expectedPlayerName = "FooBar";
    when(gameJoiner.joinGame(
        same(newGameEntity),
        argThat(params -> params.getPlayerName().equals(expectedPlayerName))))
        .thenReturn(expectedPlayerRef);

    NewGameController controller =
        new NewGameController(gameFactory, gameDaoProviderInterface, gameJoiner);

    GameRef gameRef = controller.newGame(expectedPlayerName, 2);
    assertEquals(newGameEntity.getId(), gameRef.getGameId());
    assertEquals(expectedPlayerRef, gameRef.getPlayerToken());
  }
}
