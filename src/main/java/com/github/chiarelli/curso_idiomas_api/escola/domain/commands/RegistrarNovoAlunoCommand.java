package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;

import io.jkratz.mediator.core.Request;

public class RegistrarNovoAlunoCommand implements Request<AlunoInterface> {
  
  private final String nome;
  private final String cpf;
  private final String email;
  private final Set<UUID> turmaMatricularIds;

  public RegistrarNovoAlunoCommand(String nome, String cpf, String email, Set<UUID> turmaMatricularIds) {
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
    this.turmaMatricularIds = turmaMatricularIds;
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

  public Set<UUID> getTurmaMatricularIds() {
    return Collections.unmodifiableSet(turmaMatricularIds);
  }

  public static Aluno toDomain(UUID alunoId, RegistrarNovoAlunoCommand cmd) {
    return new Aluno(alunoId, cmd.getNome(), cmd.getCpf(), cmd.getEmail());
  }
}
