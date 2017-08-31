package net.rk.splendid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public final class CommonSessionParameters {
  private String gameRef;
  private String playerToken;

  public CommonSessionParameters() {}

  void setGameRef(String gameRef) {
    this.gameRef = gameRef;
  }

  void setPlayerToken(String playerToken) {
    this.playerToken = playerToken;
  }

  String getGameRef() {
    if (gameRef != null) {
      return gameRef;
    } else {
      throw new IllegalStateException("gameRef not set");
    }
  }

  public String getPlayerToken() {
    if (playerToken != null) {
      return playerToken;
    } else {
      throw new IllegalStateException("playerToken not set");
    }
  }
}
