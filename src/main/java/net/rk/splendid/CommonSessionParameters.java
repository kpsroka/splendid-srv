package net.rk.splendid;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;

@Component
@Scope(
    scopeName = WebApplicationContext.SCOPE_SESSION,
    proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonSessionParameters implements Serializable {
  private String gameRef;
  private String playerToken;

  public CommonSessionParameters() {}

  void setGameRef(String gameRef) {
    this.gameRef = gameRef;
  }

  void setPlayerToken(String playerToken) {
    this.playerToken = playerToken;
  }

  public String getGameRef() {
    if (gameRef != null) {
      return gameRef;
    } else {
      throw new NullPointerException("[@" + this.hashCode() + "] Null gameRef.");
    }
  }

  public String getPlayerToken() {
    if (playerToken != null) {
      return playerToken ;
    } else {
      throw new NullPointerException("[@" + this.hashCode() + "] Null playerToken.");
    }
  }
}
