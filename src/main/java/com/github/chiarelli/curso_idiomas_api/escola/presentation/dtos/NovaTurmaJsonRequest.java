package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"numero", "ano_letivo"})
public class NovaTurmaJsonRequest {

  private final Integer numero;

  @JsonProperty("ano_letivo")
  private final Integer anoLetivo;
  
  public NovaTurmaJsonRequest(Integer numero, Integer anoLetivo) {
    this.numero = numero;
    this.anoLetivo = anoLetivo;
  }

  public Integer getNumero() {
    return numero;
  }

  public Integer getAnoLetivo() {
    return anoLetivo;
  }

}
