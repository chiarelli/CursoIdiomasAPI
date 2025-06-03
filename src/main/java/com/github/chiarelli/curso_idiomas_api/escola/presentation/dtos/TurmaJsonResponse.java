package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "numero", "ano_letivo", "aluno_ids"})
public class TurmaJsonResponse extends NovaTurmaJsonRequest {

  private final UUID id;

  @JsonProperty("aluno_ids")
  private final Set<UUID> alunoIds;

  public TurmaJsonResponse(UUID id, Integer numero, Integer anoLetivo, Set<UUID> alunoIds) {
    super(numero, anoLetivo);
    this.id = id;
    if(alunoIds == null) {
      alunoIds = Set.of();
    }
    this.alunoIds = alunoIds;
  }

  public UUID getId() {
    return id;
  }

  public Set<UUID> getAlunoIds() {
    return Collections.unmodifiableSet(alunoIds);
  }

}
