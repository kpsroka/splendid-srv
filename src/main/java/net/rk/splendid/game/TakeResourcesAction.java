package net.rk.splendid.game;

import com.google.common.collect.Iterables;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayerHand;
import net.rk.splendid.dao.entities.OfyResourceMap;

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

  @Override
  public OfyGameState apply(String playerToken, OfyGameState gameState) {
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
    OfyPlayerHand playerHand = gameState.getPlayerState(playerToken).getHand();
    playerHand.setResources(playerHand.getResources().join(takenResources));

    return gameState;
  }

  private boolean meetsActionCriteria(Map<Integer, Long> takenResourceMap) {
    return (takenResourceMap.size() == 1 && Iterables.getOnlyElement(takenResourceMap.values()).equals(2L))
        || (takenResourceMap.size() == 3 && takenResourceMap.values().stream().allMatch(value -> value.equals(1L)));
  }
}
