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
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.rk.splendid.RestyGameController.REQUEST_PARAMS_ATTRIBUTE_KEY;
import static net.rk.splendid.game.GameActionContextMatcher.hasPayload;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class RestyGameControllerTest {
  @Mock private GameDaoProvider gameDaoProvider;
  @Mock private GameJoiner gameJoiner;
  @Mock private RequestContext requestContext;
  @Mock private GameDao gameDao;

  private GameActionProvider emptyGameActionProvider =
      new GameActionProvider(Lists.newArrayList());
  private FactoryGenerator factoryGenerator =
      i -> new OfyResourceFactory(i, i, new OfyResourceMap(Lists.newArrayList()));

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void returnsGameStatus() {
    GameStatus expectedStatus = new GameStatus("StAtUs");
    when(gameDao.getGameStatus()).thenReturn(expectedStatus);
    when(gameDaoProvider.getGameDao(same(requestContext))).thenReturn(gameDao);

    RestyGameController controller =
        new RestyGameController(gameDaoProvider, emptyGameActionProvider, gameJoiner);
    Assert.assertEquals(expectedStatus, controller.getStatus(requestContext));
  }

  @Test
  public void returnsGameState() {
    String playerToken = "playerRef1";
    when(requestContext.getPlayerToken()).thenReturn(playerToken);
    GameEntity gameEntity = createGameEntityWithPlayerRefs(playerToken);
    when(gameDao.getGameEntity()).thenReturn(gameEntity);
    when(gameDaoProvider.getGameDao(same(requestContext))).thenReturn(gameDao);

    RestyGameController controller =
        new RestyGameController(gameDaoProvider, emptyGameActionProvider, gameJoiner);
    AssertionHelper.assertStringEquality(
        OfyGameState.toDto(gameEntity.getGameState(), gameEntity.getGameConfig(), playerToken),
        controller.getState(requestContext, -1)
    );
  }

  @Test
  public void returnsGameConfig() {
    String playerToken = "playerRef1";
    when(requestContext.getPlayerToken()).thenReturn(playerToken);
    GameConfig expectedConfig =
        new GameConfig(
            new GameRef("foo", "bar"),
            new Player[] { new Player("player 1") });
    when(gameDao.getGameConfig()).thenReturn(expectedConfig);
    when(gameDaoProvider.getGameDao(same(requestContext))).thenReturn(gameDao);

    RestyGameController controller =
        new RestyGameController(gameDaoProvider, emptyGameActionProvider, gameJoiner);
    Assert.assertSame(expectedConfig, controller.getConfig(requestContext));
  }

  @Test
  public void executesAction() {
    String actionType = "7yp3";
    String actionPayload = "p4y104d";
    String playerToken = "playerRef1";

    OfyGameState newState = OfyGameState.create(new String[] { playerToken, "New Player Token" }, factoryGenerator);
    newState.incrementRound();

    GameAction action = Mockito.mock(GameAction.class);
    when(action.getActionType()).thenReturn(actionType);
    when(action.apply(argThat(hasPayload(actionPayload)), any())).thenReturn(newState);

    when(requestContext.getPlayerToken()).thenReturn(playerToken);
    GameEntity gameEntity = createGameEntityWithPlayerRefs(playerToken);
    when(gameDao.getGameEntity()).thenReturn(gameEntity);
    when(gameDaoProvider.getGameDao(same(requestContext))).thenReturn(gameDao);

    RestyGameController controller =
        new RestyGameController(gameDaoProvider, new GameActionProvider(Lists.newArrayList(action)), gameJoiner);

    GameState returnedState = controller.act(requestContext, actionType, actionPayload);
    AssertionHelper.assertStringEquality(
        OfyGameState.toDto(newState, gameEntity.getGameConfig(), playerToken),
        returnedState
    );

    verify(gameDao).updateGameState(same(newState));
  }

  @Test
  public void joinsPlayer() {
    String playerToken = "foo bar baz";
    GameEntity providedEntity = createGameEntityWithPlayerRefs(playerToken);
    when(gameDao.getGameEntity()).thenReturn(providedEntity);
    when(gameDaoProvider.getGameDao(requestContext)).thenReturn(gameDao);

    String playerName = "PlAyEr_NaMe";
    String newPlayerToken = "New_Player_Token";
    when(gameJoiner.joinGame(same(providedEntity), argThat(hasPlayerName(playerName))))
        .thenReturn(newPlayerToken);

    RestyGameController controller = new RestyGameController(gameDaoProvider, emptyGameActionProvider, gameJoiner);
    GameRef returnedGameRef = controller.join(requestContext, playerName);

    Assert.assertEquals(providedEntity.getId(), returnedGameRef.getGameId());
    Assert.assertEquals(newPlayerToken, returnedGameRef.getPlayerToken());
  }

  @Test
  public void joinThrowsOnPlayerNameEmpty() {
    RestyGameController controller = new RestyGameController(gameDaoProvider, emptyGameActionProvider, gameJoiner);
    try {
      controller.join(requestContext, "");
      Assert.fail("Should have thrown PlayerNameEmptyException.");
    } catch (PlayerNameEmptyException expected) {
      // pass!
    }
  }

  private ArgumentMatcher<JoinGameParameters> hasPlayerName(String playerName) {
    return argument -> argument.getPlayerName().equals(playerName);
  }

  @Test
  public void setsRequestContext() {
    Model model = Mockito.mock(Model.class);

    RestyGameController controller =
        new RestyGameController(gameDaoProvider, emptyGameActionProvider, gameJoiner);
    String gameId = "123-game-ID";
    String playerToken = "plr-tkn";
    controller.handleCommonAttributes(model, gameId, playerToken);

    // Captors don't perform any type checks, so we need to filter calls manually.
    ArgumentCaptor<Object> requestContextCaptor = ArgumentCaptor.forClass(Object.class);
    verify(model, times(1))
        .addAttribute(eq(REQUEST_PARAMS_ATTRIBUTE_KEY), requestContextCaptor.capture());
    List<RequestContext> capturedRequestContexts =
        requestContextCaptor.getAllValues().stream()
            .filter(object -> object instanceof RequestContext)
            .map(object -> (RequestContext) object)
            .collect(Collectors.toList());
    Assert.assertEquals(1, capturedRequestContexts.size());

    RequestContext capturedContext = capturedRequestContexts.get(0);
    Assert.assertEquals(gameId, capturedContext.getGameRef());
    Assert.assertEquals(playerToken, capturedContext.getPlayerToken());
  }

  private GameEntity createGameEntityWithPlayerRefs(String... playerRefs) {
    return new GameEntity(
        OfyGameConfig.create(
            IntStream
                .range(0, playerRefs.length)
                .mapToObj(i -> OfyPlayer.create(i, "player-" + i))
                .toArray(OfyPlayer[]::new),
            playerRefs),
        OfyGameState.create(playerRefs, factoryGenerator));
  }
}
