package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Player;

public class OfyPlayer {
  private String name;
  private int index;
  private boolean joined = false;

  private OfyPlayer() {}

  private OfyPlayer(String playerName, int playerIndex) {
    this.name = playerName;
    this.index = playerIndex;
  }

  int getIndex() {
    return index;
  }

  static Player toDto(OfyPlayer ofyPlayer) {
    return new Player(ofyPlayer.name);
  }

  public static OfyPlayer create(int index, String name) {
    return new OfyPlayer(name, index);
  }

  public boolean hasJoined() {
    return false;
  }

  void setJoined() {
    this.joined = true;
  }

  void setName(String name) {
    this.name = name;
  }
}
