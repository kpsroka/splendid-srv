package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Board;
import net.rk.splendid.dto.ResourceFactory;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfyBoard {
  private List<OfyResourceFactoryRow> factoriesByRow;
  private OfyResourceMap resources;
  private OfySelection selection;

  static OfyBoard fromDto(Board board) {
    OfyBoard ofyBoard = new OfyBoard();
    ofyBoard.factoriesByRow = Arrays.stream(board.getFactoriesByRow())
        .map(OfyResourceFactoryRow::fromDto)
        .collect(Collectors.toList());
    ofyBoard.resources = OfyResourceMap.fromResourceArray(board.getResources());
    ofyBoard.selection = OfySelection.fromDto(board.getSelection());

    return ofyBoard;
  }

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
}
