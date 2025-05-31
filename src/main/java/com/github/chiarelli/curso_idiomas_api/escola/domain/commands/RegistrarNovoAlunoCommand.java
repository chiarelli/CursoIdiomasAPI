package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;

import io.jkratz.mediator.core.Request;

public class RegistrarNovoAlunoCommand implements Request<AlunoInterface> {
  
  private final String nome;
  private final String cpf;
  private final String email;
  private final Set<Integer> turmaMatricularIds;

  public RegistrarNovoAlunoCommand(String nome, String cpf, String email, Set<Integer> turmaMatricularIds) {
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
    if(turmaMatricularIds == null) {
      turmaMatricularIds = Set.of();
    }
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

  public Set<Integer> getTurmaMatricularIds() {
    return new HashSet<>(turmaMatricularIds);
  }

  public static Aluno toDomain(UUID alunoId, RegistrarNovoAlunoCommand aluno) {
    return new Aluno(alunoId, aluno.getNome(), aluno.getCpf(), aluno.getEmail());
  }
}
