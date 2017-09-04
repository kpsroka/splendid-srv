package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Board;
import net.rk.splendid.dto.ResourceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class OfyBoard {
  private static final int FACTORY_ROWS = 3;

  private List<OfyResourceFactoryRow> factoriesByRow = new ArrayList<>();
  private OfyResourceMap resources;
  private OfySelection selection;

  public static Board toDto(OfyBoard ofyBoard) {
    return new Board(
        ofyBoard.factoriesByRow.stream()
            .map(OfyResourceFactoryRow::toDto)
            .toArray(ResourceFactory[][]::new),
        OfyResourceMap.toResourceArray(ofyBoard.resources),
        OfySelection.toDto(ofyBoard.selection));
  }

  public OfyResourceFactory getFactory(int rowIndex, int itemIndex) {
    OfyResourceFactoryRow factoryRow = factoriesByRow.get(rowIndex);
    return factoryRow.getFactory(itemIndex);
  }

  public void setFactory(
      int rowIndex,
      int itemIndex,
      OfyResourceFactory resourceFactory) {
    factoriesByRow.get(rowIndex).setFactory(itemIndex, resourceFactory);
  }

  public OfyResourceMap getResources() {
    return resources;
  }

  public void setResources(OfyResourceMap resources) {
    this.resources = resources;
  }

  static OfyBoard create() {
    OfyBoard board = new OfyBoard();
    IntStream.range(0, FACTORY_ROWS).forEach(
        rowIndex -> board.factoriesByRow.add(OfyResourceFactoryRow.create(rowIndex)));
    board.selection = new OfyNoSelection();
    board.resources = OfyResourceMap.createInitialBoardMap();
    return board;
  }
}
