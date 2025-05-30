package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;

import io.jkratz.mediator.core.Request;

public class RegistrarNovoAlunoCommand implements Request<AlunoInterface> {
  
  private final String nome;
  private final String cpf;
  private final String email;

  public RegistrarNovoAlunoCommand(String nome, String cpf, String email) {
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

  public static Aluno toDomain(UUID alunoId, RegistrarNovoAlunoCommand aluno) {
    return new Aluno(alunoId, aluno.getNome(), aluno.getCpf(), aluno.getEmail());
  }
}
