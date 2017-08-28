package net.rk.splendid.dto;

public final class Board {
  private final ResourceFactory[][] factoriesByRow;
  private final int[] resources;
  private final Selection selection;

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
}
