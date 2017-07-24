package net.rk.splendid.dao;

public final class Board {
  private final ResourceFactory[][] factoriesByRow;
  private final int[] resources;

  public Board(ResourceFactory[][] factoriesByRow, int[] resources) {
    this.factoriesByRow = factoriesByRow;
    this.resources = resources;
  }

  public ResourceFactory[][] getFactoriesByRow() {
    return factoriesByRow;
  }

  public int[] getResources() {
    return resources;
  }
}
