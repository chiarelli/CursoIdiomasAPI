package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlunoJsonResponse {

  private final UUID id;
  private final String nome;
  private final String email;
  private final String cpf;
  
  @JsonProperty("turmas_matriculadas")
  private final Set<TurmaRefJson> turmasMatriculadas;

  public AlunoJsonResponse(UUID id, String nome, String email, String cpf, Set<TurmaRefJson> turmasMatriculadas) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.cpf = cpf;
    this.turmasMatriculadas = turmasMatriculadas;
  }

  public UUID getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public String getCpf() {
    return cpf;
  }

  public Set<TurmaRefJson> getTurmasMatriculadas() {
    return turmasMatriculadas;
  }

}
