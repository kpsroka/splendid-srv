package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Ignore;
import net.rk.splendid.dto.NoSelection;
import net.rk.splendid.dto.Selection;

enum SelectionType {
  NO_SELECTION
}

abstract class OfySelection {
  @Ignore SelectionType selectionType;

  static Selection toDto(OfySelection ofySelection) {
    if (ofySelection.selectionType.equals(SelectionType.NO_SELECTION)) {
      return new NoSelection();
    } else {
      throw new IllegalArgumentException("Not supported type: " + ofySelection.selectionType);
    }
  }
}
