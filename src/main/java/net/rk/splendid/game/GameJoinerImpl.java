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
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameConfig;
import net.rk.splendid.dao.entities.OfyGameStatus;
import net.rk.splendid.dao.entities.OfyPlayer;
import net.rk.splendid.exceptions.AllPlayersJoinedException;
import net.rk.splendid.exceptions.DuplicatePlayerNameException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class GameJoinerImpl implements GameJoiner {
  public String joinGame(GameEntity gameEntity, JoinGameParameters parameters) {
    OfyGameConfig gameConfig = gameEntity.getGameConfig();
    Map<String, OfyPlayer> players = gameConfig.getPlayersOrdered();

    if (players.values().stream()
        .anyMatch(player -> player.getName().equals(parameters.getPlayerName()))) {
      throw new DuplicatePlayerNameException(parameters.getPlayerName());
    }

    Map.Entry<String, OfyPlayer> joinedPlayer = Iterables.getFirst(
        players.entrySet().stream()
            .filter(e -> !e.getValue().hasJoined())
            .collect(Collectors.toList()),
        null
    );

    if (joinedPlayer == null) {
      throw new AllPlayersJoinedException();
    }

    gameConfig.setPlayerName(joinedPlayer.getKey(), parameters.getPlayerName());
    gameConfig.setPlayerJoined(joinedPlayer.getKey());

    if (players.values().stream()
        .map(OfyPlayer::hasJoined)
        .reduce(true, Boolean::logicalAnd)) {
      gameEntity.getGameState().setGameStatus(OfyGameStatus.UNDERWAY);
    }

    return joinedPlayer.getKey();
  }
}
