package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlunoJsonResponse {

  private final UUID id;
  private final String nome;
  private final String email;
  private final String cpf;
  
  @JsonProperty("turma_ids")
  private final Set<UUID> turmaIds;

  public AlunoJsonResponse(UUID id, String nome, String email, String cpf, Set<UUID> turmasIds) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.cpf = cpf;
    this.turmaIds = turmasIds;
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

  public Set<UUID> getTurmaIds() {
    return turmaIds;
  }

}
