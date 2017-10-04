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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class TakeResourcesActionTest {
  private static final String[] PLAYER_REFS =
      new String[] { "PlayerOne", "PlayerTwo", "PlayerThree", "PlayerFour" };
  private static final OfyResourceFactory DEFAULT_FACTORY =
      new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList(0)));

  @Rule public MockitoRule strictMocksRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
  @Mock private FactoryGenerator factoryGeneratorMock;

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
    when(factoryGeneratorMock.apply(ArgumentMatchers.anyInt())).thenReturn(DEFAULT_FACTORY);
  }

  @Test
  public void throwsOnMalformedPayload() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("abc", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalArgumentException.");
    } catch (IllegalArgumentException exception) {
      // pass
    }
  }

  @Test
  public void throwsOnIllegalPayload_tooManySingleTypeResources() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("1,1,1", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    gameState.getBoard().setResources(
        new OfyResourceMap(Lists.newArrayList(1, 1, 1)));

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalArgumentException.");
    } catch (IllegalArgumentException exception) {
      // pass
    }
  }

  @Test
  public void throwsOnIllegalPayload_badResourceMix() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("1,2,2", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    gameState.getBoard().setResources(
        new OfyResourceMap(Lists.newArrayList(1, 2, 2)));

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalArgumentException.");
    } catch (IllegalArgumentException exception) {
      // pass
    }
  }


  @Test
  public void throwsOnIllegalPayload_tooManyResourcesTaken() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("1,2,3,4", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    gameState.getBoard().setResources(
        new OfyResourceMap(Lists.newArrayList(1, 2, 3, 4)));

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalArgumentException.");
    } catch (IllegalArgumentException exception) {
      // pass
    }
  }

  @Test
  public void throwsOnInsufficientResourcesOnBoard() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("1,2,3", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    gameState.getBoard().setResources(
        new OfyResourceMap(Lists.newArrayList(2, 3, 4)));

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalStateException.");
    } catch (IllegalStateException exception) {
      // pass
    }
  }

  @Test
  public void addsTakenResourcesToPlayerHand() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("1,2,3", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    gameState.getBoard().setResources(
        new OfyResourceMap(Lists.newArrayList(1, 2, 3)));
    gameState.getPlayerState(PLAYER_REFS[0]).getHand().setResources(
        new OfyResourceMap(Lists.newArrayList(2, 3, 4)));

    OfyGameState newState = action.apply(context, gameState);

    Assert.assertEquals(
        ImmutableMap.of(1, 1L, 2, 2L, 3, 2L, 4, 1L),
        newState.getPlayerState(PLAYER_REFS[0]).getHand().getResources().asMap());
  }

  @Test
  public void removesTakenResourcesFromBoard() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    GameActionContext context = new GameActionContext("2,2", PLAYER_REFS[0]);
    TakeResourcesAction action = new TakeResourcesAction();

    gameState.getBoard().setResources(
        new OfyResourceMap(Lists.newArrayList(1, 2, 2, 2)));

    OfyGameState newState = action.apply(context, gameState);

    Assert.assertEquals(
        ImmutableMap.of(1, 1L, 2, 1L),
        newState.getBoard().getResources().asMap());
  }
}
