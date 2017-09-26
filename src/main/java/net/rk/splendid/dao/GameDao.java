package net.rk.splendid.dao;

import com.google.common.collect.Iterables;
import com.googlecode.objectify.Key;
import net.rk.splendid.CommonSessionParameters;
import net.rk.splendid.dao.entities.*;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.GameStatus;
import net.rk.splendid.exceptions.GameNotFoundException;
import net.rk.splendid.game.FactoryGenerator;
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
  @Autowired private FactoryGenerator factoryGenerator;

  public GameRef createGame(int numberOfPlayers, String playerName) {
    OfyPlayer[] players = new OfyPlayer[numberOfPlayers];
    Arrays.setAll(players, i -> OfyPlayer.create(i, i == 0 ? playerName : "Waiting…"));
    String[] playerRefs = new String[numberOfPlayers];
    Arrays.setAll(playerRefs, i -> UUID.randomUUID().toString());

    OfyGameConfig gameConfig = OfyGameConfig.create(players, playerRefs);
    gameConfig.setPlayerJoined(playerRefs[0]);

    OfyGameState gameState = OfyGameState.create(playerRefs, factoryGenerator);
    gameState.setGameStatus(OfyGameStatus.PREPARING);

    GameEntity entity = new GameEntity(gameConfig, gameState);
    ofy().save().entity(entity).now();
    return new GameRef(entity.getId(), playerRefs[0]);
  }

  public GameConfig getGameConfig() {
    String gameRefId = sessionParamsProvider.getGameRef();
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, gameRefId);
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    return OfyGameConfig.toDto(gameRefId, sessionParamsProvider.getPlayerToken(), gameEntity.getGameConfig());
  }

  private OfyGameState getGameState() {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, sessionParamsProvider.getGameRef());
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();

    if (gameEntity == null) {
      throw new GameNotFoundException();
    }

    return gameEntity.getGameState();
  }

  public void updateGameState(OfyGameState newState) {
    Key<GameEntity> gameRefKey = Key.create(GameEntity.class, this.sessionParamsProvider.getGameRef());
    GameEntity gameEntity = ofy().load().key(gameRefKey).now();
    gameEntity.setGameState(newState);
    ofy().save().entity(gameEntity);
  }

  public GameEntity getGameEntity() {
    Key<GameEntity> gameRefKey =
        Key.create(GameEntity.class, sessionParamsProvider.getGameRef());
    GameEntity entity = ofy().load().key(gameRefKey).now();
    if (entity != null) {
      return entity;
    } else {
      throw new GameNotFoundException();
    }
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

    if (players.values().stream()
        .map(OfyPlayer::hasJoined)
        .reduce(true, Boolean::logicalAnd)) {
      gameEntity.getGameState().setGameStatus(OfyGameStatus.UNDERWAY);
    }

    ofy().save().entity(gameEntity);

    return joinedPlayer.getKey();
  }

  public GameStatus getGameStatus() {
    return new GameStatus(getGameState().getGameStatus().name());
  }
}
