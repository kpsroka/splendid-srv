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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import net.rk.splendid.dao.GameDao;
import net.rk.splendid.dao.entities.*;
import net.rk.splendid.dto.*;
import net.rk.splendid.exceptions.PlayerNameEmptyException;
import net.rk.splendid.game.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class GameDataProviderTest {
  @Mock private CommonSessionParameters commonSessionParameters;
  @Mock private GameJoiner gameJoiner;
  @Mock private GameDao gameDao;
  @Mock private GameDaoProvider gameDaoProvider;
  @Mock private FactoryGenerator factoryGenerator;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(factoryGenerator.apply(anyInt())).thenReturn(
        new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList())));
  }

  @Test
  public void returnsGameStatusForPlayer() {
    GameStatus providedStatus = new GameStatus("expectedStatus");
    when(gameDao.getGameStatus()).thenReturn(providedStatus);
    when(gameDaoProvider.getGameDao(any())).thenReturn(gameDao);

    GameDataController controller = new GameDataController(
        gameDaoProvider,
        gameJoiner,
        commonSessionParameters,
        new GameActionProvider(Lists.newArrayList()));

    Assert.assertEquals(providedStatus, controller.getGameStatus());
  }

  @Test
  public void returnsGameConfigForPlayer() {
    GameConfig providedConfig = new GameConfig(new GameRef("", ""), new Player[]{});
    when(gameDao.getGameConfig()).thenReturn(providedConfig);
    when(gameDaoProvider.getGameDao(any())).thenReturn(gameDao);

    GameDataController controller = new GameDataController(
        gameDaoProvider,
        gameJoiner,
        commonSessionParameters,
        new GameActionProvider(Lists.newArrayList()));

    Assert.assertEquals(providedConfig, controller.getGameConfig());
  }

  @Test
  public void returnsGameEntityForPlayer() {
    String playerToken = "FooBarBaz";

    GameEntity providedEntity = new GameEntity(
        OfyGameConfig.create(
            new OfyPlayer[] { OfyPlayer.create(0, playerToken)},
            new String[] { playerToken }),
        OfyGameState.create(
            new String[] { playerToken }, factoryGenerator));

    when(gameDao.getGameEntity()).thenReturn(providedEntity);
    when(gameDaoProvider.getGameDao(any())).thenReturn(gameDao);
    when(commonSessionParameters.getPlayerToken()).thenReturn(playerToken);

    GameDataController controller = new GameDataController(
        gameDaoProvider,
        gameJoiner,
        commonSessionParameters,
        new GameActionProvider(Lists.newArrayList()));

    GameState expectedGameState =
        OfyGameState.toDto(providedEntity.getGameState(), providedEntity.getGameConfig(), playerToken);
    GameState actualGameState = controller.getGameState(-1);

    assertStringEquality(expectedGameState, actualGameState);
  }

  @Test
  public void executesAction() {
    String playerToken = "foobarbaz";
    GameEntity providedEntity = new GameEntity(
        OfyGameConfig.create(
            new OfyPlayer[] { OfyPlayer.create(0, playerToken)},
            new String[] { playerToken }),
        OfyGameState.create(
            new String[] { playerToken }, factoryGenerator));
    when(gameDao.getGameEntity()).thenReturn(providedEntity);
    when(gameDaoProvider.getGameDao(any())).thenReturn(gameDao);
    when(commonSessionParameters.getPlayerToken()).thenReturn(playerToken);

    String actionType = "AcTiOn_TyPe";
    String actionPayload = "PaYlOaD";
    OfyGameState newGameState = OfyGameState.create(new String[] { playerToken }, factoryGenerator);

    GameAction gameAction = mock(GameAction.class);
    when(gameAction.getActionType()).thenReturn(actionType);
    when(gameAction.apply(
        argThat(GameActionContextMatcher.hasPayload(actionPayload)),
        eq(providedEntity.getGameState())))
        .thenReturn(newGameState);

    GameDataController controller = new GameDataController(
        gameDaoProvider,
        gameJoiner,
        commonSessionParameters,
        new GameActionProvider(Lists.newArrayList(gameAction)));

    GameState returnedState = controller.executePlayerAction(actionType, actionPayload);
    GameState expectedState = OfyGameState.toDto(newGameState, providedEntity.getGameConfig(), playerToken);

    assertStringEquality(expectedState, returnedState);

    verify(gameDao).updateGameState(newGameState);
  }

  @Test
  public void throwsOnPlayerJoinWithEmptyName() {
    GameDataController controller = new GameDataController(
        gameDaoProvider,
        gameJoiner,
        commonSessionParameters,
        new GameActionProvider(Lists.newArrayList()));

    try {
      controller.joinGame("");
      Assert.fail("Expected PlayerNameEmptyException to be thrown");
    } catch (PlayerNameEmptyException expected) {
      // pass!
    }
  }

  @Test
  public void joinsNewPlayer() {
    String playerToken = "foobarbaz";
    GameEntity providedEntity = new GameEntity(
        OfyGameConfig.create(
            new OfyPlayer[] { OfyPlayer.create(0, playerToken)},
            new String[] { playerToken }),
        OfyGameState.create(
            new String[] { playerToken }, factoryGenerator));
    when(gameDao.getGameEntity()).thenReturn(providedEntity);
    when(gameDaoProvider.getGameDao(any())).thenReturn(gameDao);

    String playerName = "PlAyEr_NaMe";
    String newPlayerToken = "New_Player_Token";
    when(gameJoiner.joinGame(same(providedEntity), argThat(hasPlayerName(playerName))))
        .thenReturn(newPlayerToken);

    GameDataController controller = new GameDataController(
        gameDaoProvider,
        gameJoiner,
        commonSessionParameters,
        new GameActionProvider(Lists.newArrayList()));
    controller.joinGame(playerName);

    verify(commonSessionParameters).setPlayerToken(newPlayerToken);
  }

  private ArgumentMatcher<JoinGameParameters> hasPlayerName(String playerName) {
    return joinGameParameters -> joinGameParameters.getPlayerName().equals(playerName);
  }

  private void assertStringEquality(Object expectedObject, Object actualObject) {
    try {
      String expectedString = new ObjectMapper().writeValueAsString(expectedObject);
      String actualString = new ObjectMapper().writeValueAsString(actualObject);
      Assert.assertEquals(expectedString, actualString);
    } catch (JsonProcessingException exception) {
      Assert.fail(exception.getMessage());
    }
  }
}
