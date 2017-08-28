package net.rk.splendid.dao.entities;

import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.Subclass;

@Subclass
public class OfyNoSelection extends OfySelection {
  @OnLoad
  public void onLoad() {
    selectionType = SelectionType.NO_SELECTION;
  }
}
