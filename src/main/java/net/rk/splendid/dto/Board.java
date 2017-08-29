package net.rk.splendid.dto;

import java.util.Arrays;

public final class Board {
  private ResourceFactory[][] factoriesByRow;
  private int[] resources;
  private Selection selection;

  public Board(ResourceFactory[][] factoriesByRow, int[] resources, Selection selection) {
    this.factoriesByRow = factoriesByRow;
    this.resources = resources;
    this.selection = selection;
  }

  public ResourceFactory[][] getFactoriesByRow() {
    return factoriesByRow;
  }

  public int[] getResources() {
    return resources;
  }

  public Selection getSelection() {
    return selection;
  }

  Board createDeepCopy() {
    return new Board(
        Arrays.stream(factoriesByRow).map(
            factoryRow -> Arrays.stream(factoryRow)
                .map(ResourceFactory::createDeepCopy)
                .toArray(ResourceFactory[]::new))
            .toArray(ResourceFactory[][]::new),
        resources.clone(),
        selection.createDeepCopy());
  }

  public void setResources(int[] resources) {
    this.resources = resources;
  }

  public void setFactories(ResourceFactory[][] factories) {
    this.factoriesByRow = factories;
  }
}
