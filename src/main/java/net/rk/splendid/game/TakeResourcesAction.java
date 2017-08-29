package net.rk.splendid.game;

import net.rk.splendid.dto.GameState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class TakeResourcesAction implements GameAction {
  private final String payload;

  TakeResourcesAction(String payload) {
    this.payload = payload;
  }

  @Override
  public GameState apply(GameState gameState) {
    List<Integer> takenResources = Arrays.stream(payload.split(","))
        .mapToInt(Integer::valueOf)
        .boxed()
        .collect(Collectors.toList());
    List<Integer> boardResources = Arrays.stream(gameState.getBoard().getResources())
        .boxed()
        .collect(Collectors.toList());

    // TODO: check whether takenResources is legal wrt game rules.

    for (Integer resource : takenResources) {
      if (!boardResources.remove(resource)) {
        throw new IllegalStateException("Can't remove resource " + resource);
      }
    }

    GameState newState = gameState.createDeepCopy();
    newState.getBoard()
        .setResources(boardResources.stream().mapToInt(Integer::intValue).toArray());

    int[] oldPlayerHand = gameState.getPlayerState()[0].getHand().getResources();
    int[] newPlayerHand = Arrays.copyOf(oldPlayerHand, oldPlayerHand.length + takenResources.size());
    for (int i = 0; i < takenResources.size(); i++) {
      newPlayerHand[oldPlayerHand.length + i] = takenResources.get(i);
    }

    newState.getPlayerState()[0].getHand().setResources(newPlayerHand);

    return newState;
  }
}
