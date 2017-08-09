package net.rk.splendid.dto;

public final class PlayerHand {
  private final ResourceFactory[] factories;
  private final int[] resources;

  public PlayerHand(ResourceFactory[] factories, int[] resources) {
    this.factories = factories;
    this.resources = resources;
  }

  public ResourceFactory[] getFactories() {
    return factories;
  }

  public int[] getResources() {
    return resources;
  }
}
