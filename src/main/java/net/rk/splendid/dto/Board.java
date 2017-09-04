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
}
