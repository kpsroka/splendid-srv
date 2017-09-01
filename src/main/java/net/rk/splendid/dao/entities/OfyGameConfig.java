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

  static OfyGameConfig fromDto(GameConfig gameCfg, String[] playerRefs) {
    OfyGameConfig ofyGameConfig = new OfyGameConfig();
    ofyGameConfig.players = IntStream
        .range(0, gameCfg.getPlayers().length)
        .boxed()
        .collect(
            Collectors.toMap(
                index -> playerRefs[index],
                index -> OfyPlayer.fromDto(gameCfg.getPlayers()[index], index)
            )
        );

    return ofyGameConfig;
  }

  public static GameConfig toDto(
      String gameRefId,
      String playerToken,
      OfyGameConfig ofyGameConfig) {
    List<OfyPlayer> playersList = new ArrayList<>(ofyGameConfig.players.values());
    playersList.sort(Comparator.comparingInt(player -> player.index));
    int firstPlayerIndex = ofyGameConfig.players.get(playerToken).index;
    Collections.rotate(playersList, -firstPlayerIndex);

    return
        new GameConfig(
            new GameRef(gameRefId, playerToken),
            playersList.stream().map(OfyPlayer::toDto).toArray(Player[]::new));
  }
}
