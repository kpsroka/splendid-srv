package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OfyResourceFactory {
  private int color;
  private int points;
  private OfyResourceMap cost;

  private OfyResourceFactory() {}

  OfyResourceFactory(int color, int points, OfyResourceMap cost) {
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

  public int getResource() {
    return color;
  }

  public static OfyResourceFactory createFactory(int minCost, int maxCost) {
    OfyResourceFactory factory = new OfyResourceFactory();
    factory.color = OfyResourceMap.COLORS.get(new Random().nextInt(OfyResourceMap.COLORS.size()));
    factory.points = (minCost / 2) + new Random().nextInt((maxCost / 2) + 1);
    factory.cost = CreateRandomResourceMap(minCost, maxCost);
    return factory;
  }

  private static OfyResourceMap CreateRandomResourceMap(int minCost, int maxCost) {
    Random random = new Random();
    int costTotal = minCost + random.nextInt(maxCost - minCost);

    return new OfyResourceMap(
        IntStream.generate(
            () -> OfyResourceMap.COLORS.get(random.nextInt(OfyResourceMap.COLORS.size())))
        .limit(costTotal)
        .boxed()
        .collect(Collectors.toList()));
  }
}
