package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Aluno implements AlunoInterface {

  @NotNull
  private UUID alunoId;

  @Size(min = 3, max = 100)
  private String nome;

  @NotNull
  @CPF
  private String cpf;

  @Email
  private String email;

  private Set<Turma> turmas = new HashSet<>();

  public Aluno(UUID alunoId, String nome, String cpf, String email, Set<Turma> turmas) {
    this.alunoId = alunoId;
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;

    for(Turma turma : turmas) {
      turma.setAluno(this);
      setTurmaMatriculada(turma);
    }
  }

  public Aluno(UUID alunoId, String nome, String cpf, String email) {
    this.alunoId = alunoId;
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
  }

  void setTurmaMatriculada(TurmaInterface turma) {
    throw new IllegalArgumentException("implement method turmasMatriculadas");
  }

  // ##### Actions #####

  public void cadastrarAluno(Aluno aluno) {
    throw new IllegalArgumentException("implement method cadastrarAluno");
  }

  public void excluirAluno(Aluno aluno) {
    throw new IllegalArgumentException("implement method excluirAluno");
  }

  // ##### Getters #####

  @Override
  public UUID getAlunoId() {
    return alunoId;
  }

  @Override
  public String getNome() {
    return nome;
  }

  @Override
  public String getCpf() {
    return cpf;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public Set<TurmaInterface> getTurmas() {
    return new HashSet<>(turmas);
  }

}
