package net.rk.splendid;

import net.rk.splendid.dto.GameConfig;
import net.rk.splendid.dto.GameRef;
import net.rk.splendid.dto.GameState;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public final class GameDataController {
  @RequestMapping("/getConfig")
  public GameConfig getGameConfig(@RequestParam("id") String gameRefId) {
    return new GameConfig(new GameRef(gameRefId));
  }

  @RequestMapping("/getState")
  public GameState getGameState(@RequestParam("id") String gameRefId) {
    return new GameState(gameRefId);
  }

  @RequestMapping("/act")
  public GameState executePlayerAction(
      @RequestParam("id") String gameRefId,
      @RequestParam("action") String action,
      @RequestParam("player") int playerIndex,
      @RequestParam("payload") String payload) {
    return new GameState(gameRefId);
  }
}
