package com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NovaTurmaJsonRequest {
  @JsonProperty("turma_id")
  private final Integer turmaId;

  @JsonProperty("ano_letivo")
  private final Integer anoLetivo;
  
  public NovaTurmaJsonRequest(Integer turmaId, Integer anoLetivo) {
    this.turmaId = turmaId;
    this.anoLetivo = anoLetivo;
  }

  public Integer getTurmaId() {
    return turmaId;
  }

  public Integer getAnoLetivo() {
    return anoLetivo;
  }

}
