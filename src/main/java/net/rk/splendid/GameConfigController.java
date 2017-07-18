package net.rk.splendid;

import net.rk.splendid.dao.GameConfig;
import net.rk.splendid.dao.GameRef;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameConfigController {
  @RequestMapping("/getConfig")
  public GameConfig getGameConfig(@RequestParam("id") String gameRefId) {
    return new GameConfig(new GameRef(gameRefId));
  }
}
