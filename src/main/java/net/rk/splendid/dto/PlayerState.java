package net.rk.splendid.dto;

public final class PlayerState {
  private PlayerHand hand;

  public PlayerState(PlayerHand hand) {
    this.hand = hand;
  }

  public PlayerHand getHand() {
    return hand;
  }
}
