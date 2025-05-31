package com.github.chiarelli.curso_idiomas_api.boundary.application.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.chiarelli.curso_idiomas_api.escola.application.exceptions.NotFoundException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;

@ControllerAdvice
public class APIRestExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Map<String, Object>> handleUIException(DomainException ex) {
    return new ResponseEntity<>(ex.getUserMessages(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
    return new ResponseEntity<>(Map.of("not_found", ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    var cause = ex.getCause();
    if (cause instanceof JsonParseException) {
      return handleJsonParseException((JsonParseException) cause);
    }
    return handleThrowable(cause);
  }

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<Map<String, Object>> handleJsonParseException(JsonParseException ex) {
    return new ResponseEntity<>(Map.of("error", "JSON inválido"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Map<String, Object>> handleThrowable(Throwable ex) {
    // Logue para investigação (log completo no back-end)
    ex.printStackTrace();

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(Map.of(
        "error", "Erro interno no servidor. Tente novamente mais tarde."
      ));
  }

}
