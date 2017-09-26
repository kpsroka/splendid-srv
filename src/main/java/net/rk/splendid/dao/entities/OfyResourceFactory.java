package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;

public class OfyResourceFactory {
  private int color;
  private int points;
  private OfyResourceMap cost;

  private OfyResourceFactory() {}

  public OfyResourceFactory(int color, int points, OfyResourceMap cost) {
    this.color = color;
    this.points = points;
    this.cost = cost;
  }

  public static ResourceFactory toDto(OfyResourceFactory ofyResourceFactory) {
    return new ResourceFactory(
        ofyResourceFactory.color,
        OfyResourceMap.toResourceArray(ofyResourceFactory.cost),
        ofyResourceFactory.points);
  }

  public OfyResourceMap getCost() {
    return cost;
  }

  int getResource() {
    return color;
  }
}
