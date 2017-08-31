package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Player;

class OfyPlayer {
  private String name;
  int index;

  OfyPlayer() {}

  private OfyPlayer(Player player, int playerIndex) {
    this.name = player.getName();
    this.index = playerIndex;
  }

  static OfyPlayer fromDto(Player player, int index) {
    return new OfyPlayer(player, index);
  }

  static Player toDto(OfyPlayer ofyPlayer) {
    return new Player(ofyPlayer.name);
  }
}
