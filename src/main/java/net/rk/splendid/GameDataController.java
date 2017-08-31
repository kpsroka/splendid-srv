package net.rk.splendid;

import net.rk.splendid.dao.GameDao;
import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameState;
import net.rk.splendid.game.GameActions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public final class GameDataController {
  @RequestMapping("/getConfig")
  public GameConfig getGameConfig(@RequestParam("id") String gameRefId) {
    return GameDao.getGameConfig(gameRefId);
  }

  @RequestMapping("/getState")
  public GameState getGameState(@RequestParam("id") String gameRefId) {
    return GameDao.getGameState(gameRefId);
  }

  @RequestMapping("/act")
  public GameState executePlayerAction(
      @RequestParam("id") String gameRefId,
      @RequestParam("action") String action,
      @RequestParam("playerToken") String playerToken,
      @RequestParam("payload") String payload) {
    GameState oldState = GameDao.getGameState(gameRefId);
    GameState newState = GameActions.GetAction(action, payload).apply(oldState);
    GameDao.updateGameState(gameRefId, newState);
    return newState;
  }

  @RequestMapping("/join")
  public GameConfig joinGame(
      @RequestParam("id") String gameRefId,
      @RequestParam("playerName") String playerName) {
    return GameDao.getGameConfig(gameRefId);
  }
}
