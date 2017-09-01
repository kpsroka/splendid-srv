package net.rk.splendid;

import net.rk.splendid.dao.GameDao;
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
    return gameDao.getGameStateImpl();
  }

  @RequestMapping("/act")
  public GameState executePlayerAction(
      @RequestParam("action") String action,
      @RequestParam("payload") String payload) {
    GameState oldState = gameDao.getGameStateImpl();
    GameState newState = GameActions.GetAction(action, payload).apply(oldState);
    gameDao.updateGameStateImpl(newState);
    return newState;
  }

  @RequestMapping("/join")
  public GameConfig joinGame(
      CommonSessionParameters sessionParams,
      @RequestParam("playerName") String playerName) {
    return gameDao.getGameConfigImpl();
  }
}
