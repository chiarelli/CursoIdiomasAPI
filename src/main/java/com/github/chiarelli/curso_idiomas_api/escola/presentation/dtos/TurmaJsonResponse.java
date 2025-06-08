package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "numero_turma", "ano_letivo", "aluno_ids"})
public class TurmaJsonResponse extends NovaTurmaJsonRequest {

  private final UUID id;

  @JsonProperty("alunos_matriculados")
  private final Set<AlunoRefJson> alunosRef;

  public TurmaJsonResponse(UUID id, Integer numero, Integer anoLetivo, Set<AlunoRefJson> aluno) {
    super(numero, anoLetivo);
    this.id = id;
    if(aluno == null) {
      aluno = Set.of();
    }
    this.alunosRef = aluno;
  }

  public UUID getId() {
    return id;
  }

  public Set<AlunoRefJson> getAlunosRef() {
    return Collections.unmodifiableSet(alunosRef);
  }

}
