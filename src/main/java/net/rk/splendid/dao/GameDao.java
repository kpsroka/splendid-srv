package net.rk.splendid.dao;

import com.googlecode.objectify.ObjectifyService;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.Player;

import java.util.Arrays;
import java.util.UUID;

public final class GameDao {
  static {
    ObjectifyService.register(GameEntity.class);
  }

  private GameDao() {}

  private static final Player[] FIXED_PLAYERS = new Player[] {
      new Player("Adam"),
      new Player("Barbara"),
      new Player("Claude"),
      new Player("Dominique")
  };

  public static GameRef createGame(int numberOfPlayers) {
    Player[] players = Arrays.copyOf(FIXED_PLAYERS, numberOfPlayers);
    GameEntity entity =
        new GameEntity(new GameConfig(new GameRef(UUID.randomUUID().toString()), players));

    ObjectifyService.ofy().save().entity(entity).now();

    return new GameRef(entity.getId());
  }
}
