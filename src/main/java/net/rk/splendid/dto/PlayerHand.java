package net.rk.splendid.dto;

import java.util.Arrays;

public final class PlayerHand {
  private ResourceFactory[] factories;
  private int[] resources;

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
