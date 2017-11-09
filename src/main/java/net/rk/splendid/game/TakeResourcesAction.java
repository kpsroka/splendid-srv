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

import com.google.common.collect.Iterables;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayerHand;
import net.rk.splendid.dao.entities.OfyResourceMap;

import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
final class TakeResourcesAction implements GameAction {
  private static final String ACTION_TYPE = "TakeResources";

  @Override
  public OfyGameState apply(GameActionContext context, OfyGameState gameState) {
    List<Integer> takenResourcesList =
        Arrays.stream(context.getPayload().split(","))
            .mapToInt(Integer::valueOf)
            .boxed()
            .collect(Collectors.toList());

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
    OfyPlayerHand playerHand = gameState.getPlayerState(context.getPlayerToken()).getHand();
    playerHand.setResources(playerHand.getResources().join(takenResources));

    return gameState;
  }

  private boolean meetsActionCriteria(Map<Integer, Long> takenResourceMap) {
    return (takenResourceMap.size() == 1 && Iterables.getOnlyElement(takenResourceMap.values()).equals(2L))
        || (takenResourceMap.size() == 3 && takenResourceMap.values().stream().allMatch(value -> value.equals(1L)));
  }

  @Override
  public String getActionType() {
    return ACTION_TYPE;
  }
}
