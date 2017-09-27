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

import java.util.Arrays;

class TakeFactoryAction implements GameAction {
  private final int[] factoryCoords;
  private final FactoryGenerator factoryGenerator;

  TakeFactoryAction(String payload, FactoryGenerator factoryGenerator) {
    this.factoryCoords = Arrays.stream(payload.split(","))
        .mapToInt(Integer::valueOf)
        .toArray();
    this.factoryGenerator = factoryGenerator;
  }

  @Override
  public OfyGameState apply(String playerToken, OfyGameState gameState) {
    if (factoryCoords.length != 2) {
      throw new IllegalArgumentException(
          "Invalid factory coords provided: " + Arrays.toString(factoryCoords));
    }

    OfyResourceFactory factory = gameState.getBoard().getFactory(factoryCoords[0], factoryCoords[1]);
    OfyPlayerHand hand = gameState.getPlayerState(playerToken).getHand();

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
}
