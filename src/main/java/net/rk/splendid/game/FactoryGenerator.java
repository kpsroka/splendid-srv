package net.rk.splendid.game;

import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public final class FactoryGenerator implements Function<Integer, OfyResourceFactory> {
  @Override
  public OfyResourceFactory apply(Integer row) {
    int color = OfyResourceMap.COLORS.get(new Random().nextInt(OfyResourceMap.COLORS.size()));
    int cost = GetCost(row);
    int points = (cost * 2 / 3);
    OfyResourceMap costMap = CreateRandomResourceMap(cost);
    return new OfyResourceFactory(color, points, costMap);
  }

  private static int GetCost(int row) {
    int minCost = GetMinCostForRow(row);
    int maxCost = GetMaxCostForRow(row);
    return minCost + new Random().nextInt(maxCost - minCost);
  }

  private static int GetMinCostForRow(int row) {
    return 1 + (row * 2);
  }

  private static int GetMaxCostForRow(int row) {
    return 4 + (row * 3);
  }

  private static OfyResourceMap CreateRandomResourceMap(int cost) {
    Random random = new Random();

    return new OfyResourceMap(
        IntStream.generate(
            () -> OfyResourceMap.COLORS.get(random.nextInt(OfyResourceMap.COLORS.size())))
            .limit(cost)
            .boxed()
            .collect(Collectors.toList()));
  }
}
