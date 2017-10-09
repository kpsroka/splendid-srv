/*
 * Copyright 2017 K. P. Sroka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.rk.splendid.exceptions;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public final class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler
  public ResponseEntity<Object> handleException(RuntimeException exception, WebRequest request) {
    if (exception.getMessage() != null && !exception.getMessage().isEmpty()) {
      return handleExceptionInternal(
          exception,
          exception.getMessage(),
          null,
          HttpStatus.BAD_REQUEST,
          request);
    } else {
      ResponseStatus status =
          AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
      if (status != null) {
        return super.handleExceptionInternal(
            exception,
            status.reason(),
            null,
            status.code(),
            request);
      } else {
        return super.handleException(exception, request);
      }

    }

  }
}
