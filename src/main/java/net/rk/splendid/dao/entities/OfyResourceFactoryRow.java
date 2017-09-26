package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

class OfyResourceFactoryRow {
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

  void setFactory(int index, OfyResourceFactory resourceFactory) {
    Assert.notNull(resourceFactory, "Attempting to set null factory.");
    resourceFactoryList.set(index, resourceFactory);
  }

  public static OfyResourceFactoryRow create(List<OfyResourceFactory> factories) {
    OfyResourceFactoryRow factoryRow = new OfyResourceFactoryRow();
    factoryRow.resourceFactoryList.addAll(factories);
    return factoryRow;
  }
}
