package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.Subclass;
import net.rk.splendid.dto.NoSelection;
import net.rk.splendid.dto.Selection;

enum SelectionType {
  NO_SELECTION
}

@Subclass
class OfyNoSelection extends OfySelection {
  @OnLoad public void onLoad() {
    selectionType = SelectionType.NO_SELECTION;
  }
}

abstract class OfySelection {
  @Ignore SelectionType selectionType;

  static OfySelection fromDto(Selection selection) {
    if (selection.getType().equals(NoSelection.NO_SELECTION_TYPE)) {
      return new OfyNoSelection();
    } else {
      throw new IllegalArgumentException("Not supported type: " + selection.getType());
    }
  }
}
