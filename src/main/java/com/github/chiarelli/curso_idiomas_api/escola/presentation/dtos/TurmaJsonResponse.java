package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "numero", "ano_letivo", "aluno_ids"})
public class TurmaJsonResponse extends TurmaJsonRequest {

  public TurmaJsonResponse(UUID id, Integer numero, Integer anoLetivo, Set<UUID> alunoIds) {
    super(id, numero, anoLetivo, alunoIds);
  }

}
