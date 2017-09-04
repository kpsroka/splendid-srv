package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.PlayerState;

public class OfyPlayerState {
  private OfyPlayerHand playerHand;

  private OfyPlayerState() {}

  public static PlayerState toDto(OfyPlayerState ofyPlayerState) {
    return new PlayerState(OfyPlayerHand.toDto(ofyPlayerState.playerHand));
  }

  public OfyPlayerHand getHand() {
    return playerHand;
  }

  static OfyPlayerState create() {
    OfyPlayerState playerState = new OfyPlayerState();
    playerState.playerHand = OfyPlayerHand.create();
    return playerState;
  }
}
