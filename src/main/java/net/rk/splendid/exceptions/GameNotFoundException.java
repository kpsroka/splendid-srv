package net.rk.splendid.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Game does not exist")
public final class GameNotFoundException extends IllegalStateException {
}
