package net.rk.splendid.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid player count")
public final class PlayerCountOutOfRangeException extends RuntimeException {
  public PlayerCountOutOfRangeException(int givenPlayerCount) {
    super("Invalid player count: " + givenPlayerCount);
  }
}
