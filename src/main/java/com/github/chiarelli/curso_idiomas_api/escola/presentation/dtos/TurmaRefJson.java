package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaRefJson {

  private final UUID id;

  @JsonProperty("_href")
  private final String href;

  public TurmaRefJson(UUID id) {
    this.id = id;
    this.href = "/api/v1/turmas/%s".formatted(id);
  }

  public UUID getId() {
    return id;
  }

  public String getHref() {
    return href;
  }

}
