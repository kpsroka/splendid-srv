package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Player;

class OfyPlayer {
  String name;

  OfyPlayer() {}

  private OfyPlayer(Player player) {
    this.name = player.getName();
  }

  static OfyPlayer fromDto(Player player) {
    return new OfyPlayer(player);
  }
}
