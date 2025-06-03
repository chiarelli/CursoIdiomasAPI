package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NovaTurmaJsonRequest {
  
  private final UUID id;

  private final Integer numero;

  @JsonProperty("ano_letivo")
  private final Integer anoLetivo;
  
  public NovaTurmaJsonRequest(UUID id, Integer numero, Integer anoLetivo) {
    this.id = id;
    this.numero = numero;
    this.anoLetivo = anoLetivo;
  }

  public UUID getId() {
    return id;
  }

  public Integer getNumero() {
    return numero;
  }

  public Integer getAnoLetivo() {
    return anoLetivo;
  }

}
