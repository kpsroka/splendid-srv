package net.rk.splendid.dao.entities;

import net.rk.splendid.dto.Board;

import java.util.List;

class OfyBoard {
  List<List<OfyResourceFactory>> factoriesByRow;
  OfyResourceMap resources;
  OfySelection selection;

  static OfyBoard fromDto(Board board) {
    OfyBoard ofyBoard = new OfyBoard();
    ofyBoard.resources = OfyResourceMap.fromResourceArray(board.getResources());
    ofyBoard.selection = OfySelection.fromDto(board.getSelection());

    return ofyBoard;
  }
}
