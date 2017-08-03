package net.rk.splendid.dao;

public final class Board {
  private final ResourceFactory[][] factoriesByRow;
  private final int[] resources;
  private final Selection selection;

  Board(ResourceFactory[][] factoriesByRow, int[] resources, Selection selection) {
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
}
