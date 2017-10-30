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

package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OfyGameConfig {
  private Map<String, OfyPlayer> players = new HashMap<>();

  private OfyGameConfig() {}

  public static GameConfig toDto(
      String gameRefId,
      String playerToken,
      OfyGameConfig ofyGameConfig) {
    List<OfyPlayer> playersList = new ArrayList<>(ofyGameConfig.players.values());
    playersList.sort(Comparator.comparingInt(OfyPlayer::getIndex));
    int firstPlayerIndex = ofyGameConfig.players.get(playerToken).getIndex();
    Collections.rotate(playersList, -firstPlayerIndex);

    return
        new GameConfig(
            new GameRef(gameRefId, playerToken),
            playersList.stream().map(OfyPlayer::toDto).toArray(Player[]::new));
  }

  public static OfyGameConfig create(OfyPlayer[] players, String[] playerRefs) {
    OfyGameConfig ofyGameConfig = new OfyGameConfig();
    ofyGameConfig.players = IntStream
        .range(0, players.length)
        .boxed()
        .collect(
            Collectors.toMap(index -> playerRefs[index], index -> players[index]));

    return ofyGameConfig;
  }

  List<String> getPlayerTokensOrdered(String firstPlayerToken) {
    SortedMap<String, OfyPlayer> orderedPlayers =
        new TreeMap<>(Comparator.comparingInt(token -> players.get(token).getIndex()));
    orderedPlayers.putAll(players);
    List<String> orderedTokens = new ArrayList<>(orderedPlayers.keySet());
    Collections.rotate(orderedTokens, -orderedTokens.indexOf(firstPlayerToken));
    return orderedTokens;
  }

  public Map<String, OfyPlayer> getPlayersOrdered() {
    SortedMap<String, OfyPlayer> orderedPlayers =
        new TreeMap<>(Comparator.comparingInt(token -> players.get(token).getIndex()));
    orderedPlayers.putAll(players);
    return orderedPlayers;
  }

  public void setPlayerJoined(String playerToken) {
    this.players.get(playerToken).setJoined();
  }

  public void setPlayerName(String playerToken, String playerName) {
    this.players.get(playerToken).setName(playerName);
  }

  OfyPlayer getPlayer(String playerToken) {
    return this.players.get(playerToken);
  }
}
