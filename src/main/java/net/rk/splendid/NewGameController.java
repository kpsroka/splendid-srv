package net.rk.splendid;

import net.rk.splendid.dao.GameRef;
import net.rk.splendid.exceptions.PlayerCountOutOfRangeException;
import net.rk.splendid.exceptions.PlayerNameEmptyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public final class NewGameController {
  @RequestMapping("/new")
  public GameRef newGame(
      @RequestParam("playerName") String playerName,
      @RequestParam("playerCount") int playerCount) {
    if (playerName.isEmpty()) {
      throw new PlayerNameEmptyException();
    }
    if (playerCount < 2 || playerCount > 5) {
      throw new PlayerCountOutOfRangeException(playerCount);
    }

    return new GameRef(UUID.randomUUID().toString());
  }
}
