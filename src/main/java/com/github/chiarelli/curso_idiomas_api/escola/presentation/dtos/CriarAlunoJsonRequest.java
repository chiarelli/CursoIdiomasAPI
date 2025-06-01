package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriarAlunoJsonRequest extends AlunoJsonRequest {
  
  @JsonProperty("turma_matricular_ids")
  private final Set<Integer> turmaMatricularIds;

  public CriarAlunoJsonRequest(String nome, String cpf, String email, Set<Integer> turmaMatricularIds) {
    super(nome, cpf, email);
    if(turmaMatricularIds == null) {
      turmaMatricularIds = new HashSet<>();
    }
    this.turmaMatricularIds = turmaMatricularIds;
  }

  public Set<Integer> getTurmaMatricularIds() {
    return new HashSet<>(turmaMatricularIds);
  }

}
