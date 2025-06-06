package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;

import io.jkratz.mediator.core.Request;

public class AtualizarDadosAlunoCommand implements Request<AlunoInterface> {

  private final UUID alunoId;
  private final String nome;
  private final String email;

  public AtualizarDadosAlunoCommand(UUID alunoId, String nome, String email) {
    this.alunoId = alunoId;
    this.nome = nome;
    this.email = email;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

}
