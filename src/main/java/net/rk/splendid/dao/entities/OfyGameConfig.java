package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import net.rk.splendid.dto.GameConfig;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class OfyGameConfig {
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
}
