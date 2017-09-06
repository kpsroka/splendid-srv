package net.rk.splendid;

import net.rk.splendid.dao.GameDao;
import net.rk.splendid.dao.entities.GameEntity;
import net.rk.splendid.dao.entities.OfyGameState;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.game.GameActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public final class GameDataController {
  @Autowired private GameDao gameDao;
  @Autowired private CommonSessionParameters commonSessionParameters;

  @ModelAttribute
  public void handleCommonAttributes(
      @RequestParam("id") String gameRefId,
      @RequestParam(value = "playerToken", required = false) String playerToken) {
    commonSessionParameters.setGameRef(gameRefId);
    commonSessionParameters.setPlayerToken(playerToken);
  }

  @RequestMapping("/getConfig")
  public GameConfig getGameConfig() {
    return gameDao.getGameConfigImpl();
  }

  @RequestMapping("/getState")
  public GameState getGameState() {
    GameEntity gameEntity = gameDao.getGameEntity();
    return OfyGameState.toDto(
        gameDao.getGameStateImpl(),
        gameEntity.getGameConfig(),
        commonSessionParameters.getPlayerToken());
  }

  @RequestMapping("/act")
  public GameState executePlayerAction(
      @RequestParam("action") String action,
      @RequestParam("payload") String payload) {
    GameEntity gameEntity = gameDao.getGameEntity();
    OfyGameState oldState = gameEntity.getGameState();
    OfyGameState newState = GameActions.GetAction(action, payload)
        .apply(commonSessionParameters.getPlayerToken(), oldState);
    gameDao.updateGameStateImpl(newState);
    return OfyGameState.toDto(
        newState,
        gameEntity.getGameConfig(),
        commonSessionParameters.getPlayerToken());
  }

  @RequestMapping("/join")
  public GameConfig joinGame(@RequestParam("playerName") String playerName) {
    commonSessionParameters.setPlayerToken(gameDao.joinPlayer(playerName));
    return gameDao.getGameConfigImpl();
  }
}
