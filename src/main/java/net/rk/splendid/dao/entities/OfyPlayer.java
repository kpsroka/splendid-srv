package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Player;

class OfyPlayer {
  private String name;
  private int index;

  private OfyPlayer() {}

  private OfyPlayer(Player player, int playerIndex) {
    this.name = player.getName();
    this.index = playerIndex;
  }

  int getIndex() {
    return index;
  }

  static OfyPlayer fromDto(Player player, int index) {
    return new OfyPlayer(player, index);
  }

  static Player toDto(OfyPlayer ofyPlayer) {
    return new Player(ofyPlayer.name);
  }
}
