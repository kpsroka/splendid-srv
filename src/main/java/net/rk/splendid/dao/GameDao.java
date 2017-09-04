package net.rk.splendid.dao;

import com.googlecode.objectify.Key;
import net.rk.splendid.CommonSessionParameters;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameConfig;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.dto.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public final class GameDao {

  @Autowired private CommonSessionParameters sessionParamsProvider;

  private static final Player[] FIXED_PLAYERS = new Player[] {
      new Player("Adam"),
      new Player("Barbara"),
      new Player("Claude"),
      new Player("Dominique")
  };

  public GameRef createGameImpl(int numberOfPlayers) {
    return GameDao.createGame(numberOfPlayers);
  }

  private static GameRef createGame(int numberOfPlayers) {
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

  public GameConfig getGameConfigImpl() {
    return GameDao.getGameConfig(
        sessionParamsProvider.getGameRef(),
        sessionParamsProvider.getPlayerToken());
  }

  private static GameConfig getGameConfig(String gameRefId, String playerToken) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    return OfyGameConfig.toDto(gameRefId, playerToken, gameEntity.getGameConfig());
  }

  public OfyGameState getGameStateImpl() {
    return GameDao.getGameState(sessionParamsProvider.getGameRef());
  }

  private static OfyGameState getGameState(String gameRefId) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    return gameEntity.getGameState();
  }

  public void updateGameStateImpl(OfyGameState newState) {
    GameDao.updateGameState(
        this.sessionParamsProvider.getGameRef(),
        newState);
  }

  private static void updateGameState(String gameRefId, OfyGameState newState) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    gameEntity.setGameState(newState);
    ofy().save().entity(gameEntity);
  }
}
