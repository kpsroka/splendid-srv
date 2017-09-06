package net.rk.splendid.dao;

import com.google.common.collect.Iterables;
import com.googlecode.objectify.Key;
import net.rk.splendid.CommonSessionParameters;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameConfig;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dao.entities.OfyPlayer;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public final class GameDao {

  @Autowired private CommonSessionParameters sessionParamsProvider;

  private static final String[] FIXED_PLAYERS = new String[] {
      "Adam",
      "Barbara",
      "Claude",
      "Dominique",
      "Emmanuel"
  };

  private GameEntity gameEntity;

  public GameRef createGameImpl(int numberOfPlayers) {
    return GameDao.createGame(numberOfPlayers);
  }

  private static GameRef createGame(int numberOfPlayers) {
    OfyPlayer[] players = new OfyPlayer[numberOfPlayers];
    Arrays.setAll(players, i -> OfyPlayer.create(i, FIXED_PLAYERS[i]));
    String[] playerRefs = new String[numberOfPlayers];
    Arrays.setAll(playerRefs, i -> UUID.randomUUID().toString());

    GameEntity entity =
        new GameEntity(
            OfyGameConfig.create(players, playerRefs),
            OfyGameState.create(playerRefs));

    entity.getGameConfig().setPlayerJoined(playerRefs[0]);
    entity.getGameConfig().setPlayerName(playerRefs[0], FIXED_PLAYERS[0]);

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

  public GameEntity getGameEntity() {
    Key<GameEntity> gameRefKey =
        Key.create(GameEntity.class, sessionParamsProvider.getGameRef());
    return ofy().load().key(gameRefKey).now();
  }

  public String joinPlayer(String playerName) {
    GameEntity gameEntity = getGameEntity();
    Map<String, OfyPlayer> players = gameEntity.getGameConfig().getPlayersOrdered();

    Map.Entry<String, OfyPlayer> joinedPlayer = Iterables.getFirst(
        players.entrySet().stream()
            .filter(e -> !e.getValue().hasJoined())
            .collect(Collectors.toList()),
        null
    );

    if (joinedPlayer == null) {
      throw new IllegalStateException("All players joined already.");
    }

    gameEntity.getGameConfig().setPlayerName(joinedPlayer.getKey(), playerName);
    gameEntity.getGameConfig().setPlayerJoined(joinedPlayer.getKey());

    ofy().save().entity(gameEntity);

    return joinedPlayer.getKey();
  }
}
