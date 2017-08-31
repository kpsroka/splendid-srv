package net.rk.splendid;

import net.rk.splendid.dao.GameDao;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.game.GameActions;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/game")
public final class GameDataController {
  @ModelAttribute
  public void handleCommonAttributes(
      CommonSessionParameters commonSessionParameters,
      @RequestParam("id") String gameRefId,
      @RequestParam(value = "playerToken", required = false) String playerToken) {
    commonSessionParameters.setGameRef(gameRefId);
    commonSessionParameters.setPlayerToken(playerToken);
  }

  @RequestMapping("/getConfig")
  public GameConfig getGameConfig(CommonSessionParameters sessionParams) {
    return GameDao.getGameConfig(sessionParams.getGameRef());
  }

  @RequestMapping("/getState")
  public GameState getGameState(CommonSessionParameters sessionParams) {
    return GameDao.getGameState(sessionParams.getGameRef());
  }

  @RequestMapping("/act")
  public GameState executePlayerAction(
      CommonSessionParameters sessionParams,
      @RequestParam("action") String action,
      @RequestParam("payload") String payload) {
    GameState oldState = GameDao.getGameState(sessionParams.getGameRef());
    GameState newState = GameActions.GetAction(action, payload).apply(oldState);
    GameDao.updateGameState(sessionParams.getGameRef(), newState);
    return newState;
  }

  @RequestMapping("/join")
  public GameConfig joinGame(
      CommonSessionParameters sessionParams,
      @RequestParam("playerName") String playerName) {
    return GameDao.getGameConfig(sessionParams.getGameRef());
  }
}
