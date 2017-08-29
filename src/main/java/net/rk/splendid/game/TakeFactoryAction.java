package net.rk.splendid.game;

import com.google.common.primitives.Ints;
import net.rk.splendid.dto.FakeData;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.PlayerHand;
import net.rk.splendid.dto.ResourceFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TakeFactoryAction implements GameAction {
  private final String payload;

  TakeFactoryAction(String payload) {
    this.payload = payload;
  }

  @Override
  public GameState apply(GameState gameState) {
    int[] factoryCoords = Arrays.stream(payload.split(","))
        .mapToInt(Integer::valueOf)
        .toArray();

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
