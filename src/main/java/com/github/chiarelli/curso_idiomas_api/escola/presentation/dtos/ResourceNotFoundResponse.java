package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResourceNotFoundResponse {

  @Schema(description = "Mensagem de erro indicando que o recurso não foi encontrado", example = "aluno id 3fa85f64-5717-4562-b3fc-2c963f66afa6 not exists.")
  @JsonProperty("not_found")
  private final String notFound;

  public ResourceNotFoundResponse(String not_found) {
    this.notFound = not_found;
  }

  public String getNotFound() {
    return notFound;
  }

}
