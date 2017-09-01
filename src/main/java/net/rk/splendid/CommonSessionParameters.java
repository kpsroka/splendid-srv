package net.rk.splendid;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public final class CommonSessionParameters {
  private Optional<String> gameRef;
  private Optional<String> playerToken;

  public CommonSessionParameters() {}

  void setGameRef(String gameRef) {
    this.gameRef = Optional.ofNullable(gameRef);
  }

  void setPlayerToken(String playerToken) {
    this.playerToken = Optional.ofNullable(playerToken);
  }

  public String getGameRef() {
    return gameRef.get();
  }

  public String getPlayerToken() {
    return gameRef.get();
  }
}
