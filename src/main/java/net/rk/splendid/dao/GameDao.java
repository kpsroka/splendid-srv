package net.rk.splendid.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameConfig;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyNoSelection;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.Player;

import java.util.Arrays;
import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

public final class GameDao {
  static {
    ObjectifyService.register(GameEntity.class);
    ObjectifyService.register(OfyNoSelection.class);
  }

  private GameDao() {}

  private static final Player[] FIXED_PLAYERS = new Player[] {
      new Player("Adam"),
      new Player("Barbara"),
      new Player("Claude"),
      new Player("Dominique")
  };

  public static GameRef createGame(int numberOfPlayers) {
    Player[] players = new Player[numberOfPlayers];
    Arrays.setAll(players, i -> FIXED_PLAYERS[i]);
    String[] playerRefs = new String[numberOfPlayers];
    Arrays.setAll(playerRefs, i -> UUID.randomUUID().toString());

    String gameRefId = UUID.randomUUID().toString();
    GameEntity entity =
        new GameEntity(
            new GameConfig(new GameRef(gameRefId), players),
            new GameState(),
            playerRefs);

    ofy().save().entity(entity).now();

    return new GameRef(entity.getId(), playerRefs[0]);
  }

  public static GameConfig getGameConfig(String gameRefId) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    return OfyGameConfig.toDto(gameRefId, gameEntity.getGameConfig());
  }

  public static GameState getGameState(String gameRefId) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    return OfyGameState.toDto(gameEntity.getGameState());
  }

  public static void updateGameState(String gameRefId, GameState newState) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    gameEntity.setGameState(OfyGameState.fromDto(newState));
    ofy().save().entity(gameEntity);
  }
}
