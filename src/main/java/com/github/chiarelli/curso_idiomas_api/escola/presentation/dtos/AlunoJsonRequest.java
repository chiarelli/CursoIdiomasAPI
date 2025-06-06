package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

public class AlunoJsonRequest {
  private final String nome;
  private final String email;

  public AlunoJsonRequest(String nome, String email) {
    this.nome = nome;
    this.email = email;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

}
