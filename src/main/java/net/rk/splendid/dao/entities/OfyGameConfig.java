package net.rk.splendid.dao.entities;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class OfyGameConfig {
  List<OfyPlayer> players = Lists.newArrayList();

  private OfyGameConfig() {}

  OfyGameConfig(GameConfig gameCfg) {
    players = Arrays.stream(gameCfg.getPlayers())
        .map(OfyPlayer::fromDto)
        .collect(Collectors.toList());
  }
}
