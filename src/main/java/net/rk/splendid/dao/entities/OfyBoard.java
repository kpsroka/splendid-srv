package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Board;
import net.rk.splendid.dto.ResourceFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class OfyBoard {
  List<OfyResourceFactoryRow> factoriesByRow;
  OfyResourceMap resources;
  OfySelection selection;

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
}
