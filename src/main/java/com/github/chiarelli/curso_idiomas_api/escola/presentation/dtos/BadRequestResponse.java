package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Map;

public class BadRequestResponse {

  private final Map<String, Object> erros;

  public BadRequestResponse(Map<String, Object> erros) {
    this.erros = erros;
  }

  public Map<String, Object> getErros() {
    return erros;
  }

}
