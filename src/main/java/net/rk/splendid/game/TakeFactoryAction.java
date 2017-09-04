package net.rk.splendid.game;

import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayerHand;
import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;

import java.util.Arrays;

class TakeFactoryAction implements GameAction {
  private final int[] factoryCoords;

  TakeFactoryAction(String payload) {
    this.factoryCoords = Arrays.stream(payload.split(","))
        .mapToInt(Integer::valueOf)
        .toArray();
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
        // TODO: extract min/max cost parameters into a shared factory.
        OfyResourceFactory.createFactory(factoryCoords[0] + 1, factoryCoords[0] + 4));

    return gameState;
  }
}
