package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"numero_turma", "ano_letivo"})
public class NovaTurmaJsonRequest {

  @JsonProperty("numero_turma")
  private final Integer numeroTurma;

  @JsonProperty("ano_letivo")
  private final Integer anoLetivo;
  
  public NovaTurmaJsonRequest(Integer numeroTurma, Integer anoLetivo) {
    this.numeroTurma = numeroTurma;
    this.anoLetivo = anoLetivo;
  }

  public Integer getNumeroTurma() {
    return numeroTurma;
  }

  public Integer getAnoLetivo() {
    return anoLetivo;
  }

}
