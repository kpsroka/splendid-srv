package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.Subclass;

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
}
