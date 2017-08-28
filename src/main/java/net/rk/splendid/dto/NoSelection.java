package net.rk.splendid.dto;

public class NoSelection extends Selection {
  public static final String NO_SELECTION_TYPE = "NO_SELECTION";

  @Override
  public String getType() {
    return NO_SELECTION_TYPE;
  }
}
