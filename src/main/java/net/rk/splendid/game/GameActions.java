package net.rk.splendid.game;

import net.rk.splendid.dto.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class GameActions {
  private static final String TAKE_RESOURCES = "TakeResources";
  private static final String TAKE_FACTORY = "TakeFactory";

  private GameActions() {}

  public static GameAction GetAction(String name, String payload) {
    switch (name) {
      case GameActions.TAKE_RESOURCES:
        return TakeResources(payload);
      case GameActions.TAKE_FACTORY:
        return TakeFactory(payload);
      default:
        return NoOp();
    }
  }

  private static GameAction NoOp() {
    return (state -> state);
  }

  private static GameAction TakeResources(String payload) {
    return (gameState) -> {
      List<Integer> takenResources = Arrays.stream(payload.split(","))
          .mapToInt(Integer::valueOf)
          .boxed()
          .collect(Collectors.toList());
      List<Integer> boardResources = Arrays.stream(gameState.getBoard().getResources())
          .boxed()
          .collect(Collectors.toList());

      // TODO: check whether takenResources is legal wrt game rules.

      for (Integer resource : takenResources) {
        if (!boardResources.remove(resource)) {
          throw new IllegalStateException("Can't remove resource " + resource);
        }
      }

      GameState newState = gameState.createDeepCopy();
      newState.getBoard()
          .setResources(boardResources.stream().mapToInt(Integer::intValue).toArray());

      int[] oldPlayerHand = gameState.getPlayerState()[0].getHand().getResources();
      int[] newPlayerHand = Arrays.copyOf(oldPlayerHand, oldPlayerHand.length + takenResources.size());
      for (int i = 0; i < takenResources.size(); i++) {
        newPlayerHand[oldPlayerHand.length + i] = takenResources.get(i);
      }

      newState.getPlayerState()[0].getHand().setResources(newPlayerHand);

      return newState;
    };
  }

  private static GameAction TakeFactory(String payload) {
    return NoOp();
  }
}
