package com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.BadRequestResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.ResourceNotFoundResponse;

@ControllerAdvice
public class APIRestExceptionHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<BadRequestResponse> handleUIException(DomainException ex) {
    var badResp = new BadRequestResponse(ex.getUserMessages());
    return new ResponseEntity<>(badResp, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ResourceNotFoundResponse> handleNotFoundException(ResourceNotFoundException ex) {
    var notFound = new ResourceNotFoundResponse(ex.getMessage());
    return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    var cause = ex.getCause();
    if (cause instanceof JsonParseException) {
      return handleJsonParseException((JsonParseException) cause);
    }
    return handleThrowable(cause);
  }

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<BadRequestResponse> handleJsonParseException(JsonParseException ex) {
    var badResp = new BadRequestResponse(Map.of("invalid_json","JSON inválido"));
    return new ResponseEntity<>(badResp, HttpStatus.BAD_REQUEST);
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
