package net.rk.splendid;

import net.rk.splendid.dao.GameRef;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class NewGameController {
  @RequestMapping("/new")
  public GameRef newGame(
      @RequestParam("playerName") String playerName,
      @RequestParam("playerCount") int playerCount) {
    return new GameRef(UUID.randomUUID().toString());
  }
}
