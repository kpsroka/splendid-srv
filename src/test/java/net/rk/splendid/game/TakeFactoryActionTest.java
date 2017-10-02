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
import net.rk.splendid.dao.entities.OfyPlayerHand;
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
public class TakeFactoryActionTest {
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
    TakeFactoryAction action = new TakeFactoryAction(factoryGeneratorMock);
    GameActionContext context = new GameActionContext("foo foo", PLAYER_REFS[0]);
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalArgumentException.");
    } catch (IllegalArgumentException exception) {
      // pass
    }
  }

  @Test
  public void throwsOnInsufficientPlayerResources() {
    TakeFactoryAction action = new TakeFactoryAction(factoryGeneratorMock);
    GameActionContext context = new GameActionContext("0,0", PLAYER_REFS[0]);
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    gameState.getPlayerState(PLAYER_REFS[0]).getHand()
        .setResources(new OfyResourceMap(Lists.newArrayList(0, 1, 2)));
    gameState.getBoard().setFactory(
        0,
        0,
        new OfyResourceFactory(123, 123, new OfyResourceMap(Lists.newArrayList(0, 1, 1))));

    try {
      action.apply(context, gameState);
      Assert.fail("Should have thrown IllegalStateException.");
    } catch (IllegalStateException exception) {
      // pass
    }
  }

  @Test
  public void deductsPlayerResourcesFromHand() {
    OfyGameState gameState = OfyGameState.create(PLAYER_REFS, factoryGeneratorMock);
    OfyPlayerHand playerHand = gameState.getPlayerState(PLAYER_REFS[0]).getHand();
    playerHand.setResources(new OfyResourceMap(Lists.newArrayList(0, 1, 2, 2)));
    playerHand.addFactory(
        new OfyResourceFactory(0, 0, new OfyResourceMap(Lists.newArrayList())));
    playerHand.addFactory(
        new OfyResourceFactory(1, 0, new OfyResourceMap(Lists.newArrayList())));
    gameState.getBoard().setFactory(
        0,
        0,
        new OfyResourceFactory(123, 123, new OfyResourceMap(Lists.newArrayList(0, 1, 1, 2))));

    TakeFactoryAction action = new TakeFactoryAction(factoryGeneratorMock);
    GameActionContext context = new GameActionContext("0,0", PLAYER_REFS[0]);

    OfyGameState newState = action.apply(context, gameState);
    OfyPlayerHand newHand = newState.getPlayerState(PLAYER_REFS[0]).getHand();

    // With player hand of resources = [0, 1, 2, 2] and factories = [0, 1] after taking
    // a factory of cost [0, 1, 1, 2] we expect to have resources on hand = [0, 2].

    Assert.assertEquals(
        ImmutableMap.builder().put(0, 1L).put(2, 1L).build(),
        newHand.getResources().asMap());
  }

}
