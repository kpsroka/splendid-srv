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
    return players;
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
