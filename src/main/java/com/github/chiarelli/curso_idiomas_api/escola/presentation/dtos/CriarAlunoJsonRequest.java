package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CriarAlunoJsonRequest extends AlunoJsonRequest {

  private final String cpf;
  
  @JsonProperty("turma_matricular_ids")
  private final Set<UUID> turmaMatricularIds;

  public CriarAlunoJsonRequest(String nome, String cpf, String email, Set<UUID> turmaMatricularIds) {
    super(nome, email);
    this.cpf = cpf;
    this.turmaMatricularIds = turmaMatricularIds;
  }

  public Set<UUID> getTurmaMatricularIds() {
    return Collections.unmodifiableSet(turmaMatricularIds);
  }

  public String getCpf() {
    return cpf;
  }

}
