package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;

public class OfyResourceFactory {
  private int color;
  private int points;
  private OfyResourceMap cost;

  private OfyResourceFactory() {}

  public static OfyResourceFactory fromDto(ResourceFactory resourceFactory) {
    OfyResourceFactory ofyResourceFactory = new OfyResourceFactory();
    ofyResourceFactory.color = resourceFactory.getColor();
    ofyResourceFactory.points = resourceFactory.getPoints();
    ofyResourceFactory.cost = OfyResourceMap.fromResourceArray(resourceFactory.getCost());

    return ofyResourceFactory;
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

  public int getResource() {
    return color;
  }
}
