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

import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayerHand;
import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class TakeFactoryAction implements GameAction {
  private static final String ACTION_TYPE = "TakeFactory";
  private final FactoryGenerator factoryGenerator;

  @Autowired
  TakeFactoryAction(FactoryGenerator factoryGenerator) {
    this.factoryGenerator = factoryGenerator;
  }

  @Override
  public OfyGameState apply(GameActionContext context, OfyGameState gameState) {
    int[] factoryCoords =
        Arrays.stream(context.getPayload().split(","))
            .mapToInt(Integer::valueOf)
            .toArray();

    if (factoryCoords.length != 2) {
      throw new IllegalArgumentException(
          "Invalid factory coords provided: " + Arrays.toString(factoryCoords));
    }

    OfyResourceFactory factory = gameState.getBoard().getFactory(factoryCoords[0], factoryCoords[1]);
    OfyPlayerHand hand = gameState.getPlayerState(context.getPlayerToken()).getHand();

    OfyResourceMap remainingCost = factory.getCost().reduce(hand.getFactoryResources());

    if (!remainingCost.isZero()) {
      OfyResourceMap resourcesOnHand = hand.getResources();
      if (resourcesOnHand.holds(remainingCost)) {
        hand.setResources(resourcesOnHand.reduce(remainingCost));
      } else {
        throw new IllegalStateException("Not enough resources to obtain factory.");
      }
    }

    hand.addFactory(factory);
    gameState.getBoard().setFactory(
        factoryCoords[0],
        factoryCoords[1],
        factoryGenerator.apply(factoryCoords[0]));
    gameState.getBoard().setResources(
        gameState.getBoard().getResources().join(remainingCost));

    return gameState;
  }

  @Override
  public String getActionType() {
    return ACTION_TYPE;
  }
}
