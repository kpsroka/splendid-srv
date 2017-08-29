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

  PlayerHand createDeepCopy() {
    return new PlayerHand(
        Arrays.stream(factories)
            .map(ResourceFactory::createDeepCopy)
            .toArray(ResourceFactory[]::new),
        resources.clone()
        );
  }

  public void setResources(int[] resources) {
    this.resources = resources;
  }

  public void setFactories(ResourceFactory[] factories) {
    this.factories = factories;
  }
}
