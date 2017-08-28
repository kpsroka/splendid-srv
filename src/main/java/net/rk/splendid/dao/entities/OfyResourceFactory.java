package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;

class OfyResourceFactory {
  int color;
  int points;
  OfyResourceMap cost;

  private OfyResourceFactory() {}

  static OfyResourceFactory fromDto(ResourceFactory resourceFactory) {
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
}
