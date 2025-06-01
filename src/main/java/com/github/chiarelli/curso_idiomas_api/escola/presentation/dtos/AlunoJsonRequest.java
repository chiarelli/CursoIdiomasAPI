package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

public class AlunoJsonRequest {
  private final String nome;
  private final String cpf;
  private final String email;

  public AlunoJsonRequest(String nome, String cpf, String email) {
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
  }

  public String getNome() {
    return nome;
  }

  public String getCpf() {
    return cpf;
  }

  public String getEmail() {
    return email;
  }

}
