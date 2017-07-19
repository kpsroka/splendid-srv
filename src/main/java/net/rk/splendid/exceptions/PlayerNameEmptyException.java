package net.rk.splendid.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Player name empty")
public final class PlayerNameEmptyException extends RuntimeException {
}
