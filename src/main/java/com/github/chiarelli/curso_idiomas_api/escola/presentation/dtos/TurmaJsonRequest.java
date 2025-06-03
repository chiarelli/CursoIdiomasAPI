package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TurmaJsonRequest extends NovaTurmaJsonRequest {

  @JsonProperty("aluno_ids")
  private final Set<UUID> alunoIds;

  public TurmaJsonRequest(UUID id, Integer numero, Integer anoLetivo, Set<UUID> alunoIds) {
    super(id, numero, anoLetivo);
    if (alunoIds == null) {
      alunoIds = Set.of();
    }
    this.alunoIds = alunoIds;
  }

   public Set<UUID> getAlunoIds() {
    return alunoIds;
  }

}
