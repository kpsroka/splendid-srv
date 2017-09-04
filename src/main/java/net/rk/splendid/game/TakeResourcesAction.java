package net.rk.splendid.game;

import com.google.common.collect.Iterables;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayerHand;
import net.rk.splendid.dao.entities.OfyResourceMap;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.GameState;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class TakeResourcesAction implements GameAction {
  private final List<Integer> takenResourcesList;

  TakeResourcesAction(String payload) {
    this.takenResourcesList = Arrays.stream(payload.split(","))
        .mapToInt(Integer::valueOf)
        .boxed()
        .collect(Collectors.toList());
  }

  public OfyGameState apply(GameRef gameRef, OfyGameState gameState) {
    OfyResourceMap boardResources = gameState.getBoard().getResources();
    OfyResourceMap takenResources = new OfyResourceMap(takenResourcesList);

    if (!boardResources.holds(takenResources)) {
      throw new IllegalStateException("Board doesn't hold taken resources.");
    }

    Map<Integer, Long> takenResourceMap = takenResources.asMap();
    if (!meetsActionCriteria(takenResourceMap)) {
      throw new IllegalArgumentException("Taken resources don't follow game rules.");
    }

    gameState.getBoard().setResources(boardResources.reduce(takenResources));
    OfyPlayerHand playerHand = gameState.getPlayerState(gameRef.getPlayerToken()).getHand();
    playerHand.setResources(playerHand.getResources().join(takenResources));

    return gameState;
  }

  private boolean meetsActionCriteria(Map<Integer, Long> takenResourceMap) {
    return (takenResourceMap.size() == 1 && Iterables.getOnlyElement(takenResourceMap.values()).equals(2L))
        || (takenResourceMap.size() == 3 && takenResourceMap.values().stream().allMatch(value -> value.equals(1L)));
  }

  @Override
  public GameState apply(GameState gameState) {
    List<Integer> boardResources = Arrays.stream(gameState.getBoard().getResources())
        .boxed()
        .collect(Collectors.toList());

    // TODO: check whether takenResourcesList is legal wrt game rules.

    for (Integer resource : takenResourcesList) {
      if (!boardResources.remove(resource)) {
        throw new IllegalStateException("Can't remove resource " + resource);
      }
    }

    GameState newState = gameState.createDeepCopy();
    newState.getBoard()
        .setResources(boardResources.stream().mapToInt(Integer::intValue).toArray());

    int[] oldPlayerHand = gameState.getPlayerState()[0].getHand().getResources();
    int[] newPlayerHand = Arrays.copyOf(oldPlayerHand, oldPlayerHand.length + takenResourcesList.size());
    for (int i = 0; i < takenResourcesList.size(); i++) {
      newPlayerHand[oldPlayerHand.length + i] = takenResourcesList.get(i);
    }

    newState.getPlayerState()[0].getHand().setResources(newPlayerHand);

    return newState;
  }
}
