package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.PlayerState;

public class OfyPlayerState {
  private OfyPlayerHand playerHand;

  private OfyPlayerState() {}

  static OfyPlayerState fromDto(PlayerState playerState) {
    OfyPlayerState ofyPlayerState = new OfyPlayerState();
    ofyPlayerState.playerHand = OfyPlayerHand.fromDto(playerState.getHand());

    return ofyPlayerState;
  }

  public static PlayerState toDto(OfyPlayerState ofyPlayerState) {
    return new PlayerState(OfyPlayerHand.toDto(ofyPlayerState.playerHand));
  }

  public OfyPlayerHand getHand() {
    return playerHand;
  }
}
