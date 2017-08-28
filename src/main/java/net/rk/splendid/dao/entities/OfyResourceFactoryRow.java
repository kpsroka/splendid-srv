package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.ResourceFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfyResourceFactoryRow {
  List<OfyResourceFactory> resourceFactoryList;

  private OfyResourceFactoryRow() {}

  static OfyResourceFactoryRow fromDto(ResourceFactory[] factoryRow) {
    OfyResourceFactoryRow ofyFactoryRow = new OfyResourceFactoryRow();
    ofyFactoryRow.resourceFactoryList =
        Arrays.stream(factoryRow).map(OfyResourceFactory::fromDto).collect(Collectors.toList());
    return ofyFactoryRow;
  }

  static ResourceFactory[] toDto(OfyResourceFactoryRow ofyFactoryRow) {
    return ofyFactoryRow.resourceFactoryList.stream()
        .map(OfyResourceFactory::toDto)
        .toArray(ResourceFactory[]::new);
  }
}
