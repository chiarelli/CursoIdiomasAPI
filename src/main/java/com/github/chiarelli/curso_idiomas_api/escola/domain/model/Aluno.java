package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CPF;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoCadastradoEvent;

import io.jkratz.mediator.core.Mediator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Aluno implements AlunoInterface {

  public static final int QT_TURMAS_MIN = 1;

  @NotNull
  private UUID alunoId;

  @Size(min = 3, max = 100)
  private String nome;

  @NotNull
  @CPF
  private String cpf;

  @Email
  private String email;

  @Size(min = QT_TURMAS_MIN, message = "O aluno deve estar matriculado em pelo menos uma turma")
  private Set<Turma> turmas = new HashSet<>();

  public Aluno(UUID alunoId, String nome, String cpf, String email, Set<Turma> turmas) {
    this.alunoId = alunoId;
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;

    if (turmas == null) {
      turmas = Set.of();
    }

    for (Turma turma : turmas) {
      turma.addAluno(this);
    }

    this.turmas = turmas;
  }

  public Aluno(UUID alunoId, String nome, String cpf, String email) {
    this.alunoId = alunoId;
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
  }

  public void addTurma(Turma turma) {
    turmas.add(turma);
  }

  public void removerTurma(Turma turma) {
    throw new IllegalArgumentException("implement method excluirAluno");
  }

  public void adicionarTurma(Set<Turma> turmas) {
    if (turmas == null || turmas.isEmpty()) {
      throw new DomainException("Aluno deve ser matriculado em pelo menos uma turma");
    }

    for (Turma t : turmas) {
      t.addAluno(this);
      this.turmas.add(t);
    }
  }

  // ##### Actions #####

  public void cadastrarAluno(Mediator mediator) {
    mediator.emit(new AlunoCadastradoEvent(this.getAlunoId()));
  }

  public void atualizarDadosAluno(Mediator mediator) {
    throw new IllegalArgumentException("implement method atualizarDadosAluno");
  }
  
  public void excluirAlunoAluno(Mediator mediator) {
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
    return Collections.unmodifiableSet(turmas);
  }

}
