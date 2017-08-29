package net.rk.splendid.game;

public final class GameActions {
  private static final String TAKE_RESOURCES = "TakeResources";
  private static final String TAKE_FACTORY = "TakeFactory";

  private GameActions() {}

  public static GameAction GetAction(String name, String payload) {
    switch (name) {
      case GameActions.TAKE_RESOURCES:
        return new TakeResourcesAction(payload);
      case GameActions.TAKE_FACTORY:
        return new TakeFactoryAction(payload);
      default:
        return NoOp();
    }
  }

  private static GameAction NoOp() {
    return (state -> state);
  }
}
