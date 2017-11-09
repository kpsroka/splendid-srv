/*
 * Copyright 2017 K. P. Sroka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.rk.splendid.game;

import net.rk.splendid.dao.entities.OfyResourceFactory;
import net.rk.splendid.dao.entities.OfyResourceMap;

import javax.inject.Named;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
final class FactoryGeneratorImpl implements FactoryGenerator {
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
