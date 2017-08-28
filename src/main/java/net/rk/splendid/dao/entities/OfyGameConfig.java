package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfyGameConfig {
  List<OfyPlayer> players = Lists.newArrayList();

  private OfyGameConfig() {}

  static OfyGameConfig fromDto(GameConfig gameCfg) {
    OfyGameConfig ofyGameConfig = new OfyGameConfig();
    ofyGameConfig.players =
        Arrays.stream(gameCfg.getPlayers())
            .map(OfyPlayer::fromDto)
            .collect(Collectors.toList());

    return ofyGameConfig;
  }

  public static GameConfig toDto(String gameRefId, OfyGameConfig ofyGameConfig) {
    return
        new GameConfig(
            new GameRef(gameRefId),
            ofyGameConfig.players.stream().map(OfyPlayer::toDto).toArray(Player[]::new));
  }
}
