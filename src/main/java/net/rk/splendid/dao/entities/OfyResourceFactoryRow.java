package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class OfyResourceFactoryRow {
  private static final int FACTORIES_PER_ROW = 5;
  private List<OfyResourceFactory> resourceFactoryList = new ArrayList<>();

  private OfyResourceFactoryRow() {}

  static ResourceFactory[] toDto(OfyResourceFactoryRow ofyFactoryRow) {
    return ofyFactoryRow.resourceFactoryList.stream()
        .map(OfyResourceFactory::toDto)
        .toArray(ResourceFactory[]::new);
  }

  OfyResourceFactory getFactory(int index) {
    return resourceFactoryList.get(index);
  }

  public void setFactory(int index, OfyResourceFactory resourceFactory) {
    Assert.notNull(resourceFactory, "Attempting to set null factory.");
    resourceFactoryList.set(index, resourceFactory);
  }

  public static OfyResourceFactoryRow create(int rowIndex) {
    OfyResourceFactoryRow factoryRow = new OfyResourceFactoryRow();
    int minCost = GetMinCostForRow(rowIndex);
    int maxCost = GetMaxCostForRow(rowIndex);
    IntStream.range(0, FACTORIES_PER_ROW)
        .forEach(index ->
            factoryRow.resourceFactoryList.add(
                OfyResourceFactory.createFactory(minCost, maxCost)));
    return factoryRow;
  }

  private static int GetMinCostForRow(int row) {
    return 1 + (row * 2);
  }

  private static int GetMaxCostForRow(int row) {
    return 4 + (row * 3);
  }
}
