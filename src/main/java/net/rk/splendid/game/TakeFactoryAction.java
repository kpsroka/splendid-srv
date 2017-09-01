package net.rk.splendid.game;

import com.google.common.primitives.Ints;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayerHand;
import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;
import net.rk.splendid.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TakeFactoryAction implements GameAction {
  private final int[] factoryCoords;

  TakeFactoryAction(String payload) {
    this.factoryCoords = Arrays.stream(payload.split(","))
        .mapToInt(Integer::valueOf)
        .toArray();
  }

  public OfyGameState apply(GameRef gameRef, OfyGameState gameState) {
    if (factoryCoords.length != 2) {
      throw new IllegalArgumentException(
          "Invalid factory coords provided: " + Arrays.toString(factoryCoords));
    }

    OfyResourceFactory factory = gameState.getBoard().getFactory(factoryCoords[0], factoryCoords[1]);
    OfyPlayerHand hand = gameState.getPlayerState(gameRef.getPlayerToken()).getHand();

    OfyResourceMap remainingCost = factory.getCost().reduce(hand.getFactoryResources());

    if (!remainingCost.isZero()) {
      OfyResourceMap resourcesOnHand = hand.getResources();
      if (resourcesOnHand.holds(remainingCost)) {
        hand.setResources(remainingCost.reduce(remainingCost));
      } else {
        throw new IllegalStateException("Not enough resources to obtain factory.");
      }
    }

    hand.addFactory(factory);
    gameState.getBoard().setFactory(
        factoryCoords[0],
        factoryCoords[1],
        OfyResourceFactory.fromDto(FakeData.CreateRandomResourceFactory(factoryCoords[0])));

    return gameState;
  }

  @Override
  public GameState apply(GameState gameState) {
    ResourceFactory[][] factories = gameState.getBoard().getFactoriesByRow();

    ResourceFactory factory = factories[factoryCoords[0]][factoryCoords[1]];
    List<Integer> factoryCost = new ArrayList<>(Ints.asList(factory.getCost()));
    PlayerHand oldPlayerHand = gameState.getPlayerState()[0].getHand();
    factoryCost.removeAll(
        Arrays.stream(oldPlayerHand.getFactories())
            .map(ResourceFactory::getColor)
            .collect(Collectors.toList()));

    List<Integer> resourcesOnHand = new ArrayList<>(Ints.asList(oldPlayerHand.getResources()));

    if (!factoryCost.isEmpty()) {
      // TODO: verify whether there are enough resources on hand
      resourcesOnHand.removeAll(factoryCost);
    }

    GameState newGameState = gameState.createDeepCopy();
    ResourceFactory newFactory = FakeData.CreateRandomResourceFactory(factoryCoords[0]);
    factories[factoryCoords[0]][factoryCoords[1]] = newFactory;
    newGameState.getBoard().setFactories(factories);

    newGameState.getPlayerState()[0].getHand()
        .setResources(resourcesOnHand.stream().mapToInt(Integer::intValue).toArray());
    newGameState.getPlayerState()[0].getHand()
        .setFactories(
            Stream.concat(
                Arrays.stream(oldPlayerHand.getFactories()),
                Stream.of(factory))
                .toArray(ResourceFactory[]::new));

    return newGameState;
  }
}
