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

import com.google.common.collect.Maps;
import net.rk.splendid.exceptions.NoSuchActionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public final class GameActionProvider {
  private final Map<String, GameAction> gameActionMap;

  @Autowired
  public GameActionProvider(List<GameAction> gameActions) {
    gameActionMap = Maps.uniqueIndex(gameActions, GameAction::getActionType);
  }

  public GameAction getAction(String actionType) {
    if (gameActionMap.containsKey(actionType)) {
      return gameActionMap.get(actionType);
    } else {
      throw new NoSuchActionException(actionType);
    }
  }
}
