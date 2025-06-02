package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Turma implements TurmaInterface {

  public static final int LOTACAO_MAX = 5;

  private UUID turmaId;

  @Min(1)
  @NotNull
  private Integer numeroTurma;
  
  @NotNull
  @Min(1900)
  @Max(2399)
  private Integer anoLetivo;

  @Size(max = LOTACAO_MAX, message = "Lotação da turma excedida")
  private Set<Aluno> alunos = new HashSet<>();

  public Turma(UUID turmaId, Integer turmaNumero, Integer anoLetivo, Set<Aluno> alunos) {
    this(turmaId, turmaNumero, anoLetivo);
    this.alunos = alunos;
  }

  public Turma(UUID turmaId, Integer turmaNumero, Integer anoLetivo) {
    this.turmaId = turmaId;
    this.numeroTurma = turmaNumero;
    alterarAnoLetivo(anoLetivo);
  }

  // ##### Actions #####

  public void alterarAnoLetivo(Integer anoLetivo) {
    this.anoLetivo = anoLetivo;
  }

  public void adicionarAluno(Aluno aluno) {
    if(alunos.size() > LOTACAO_MAX) {
      throw new DomainException("Lotação da turma excedida");
    }
    alunos.add(aluno);
  }

  public void removerAluno(Aluno aluno) {
    throw new IllegalArgumentException("implement method removerAluno");
  }

  // ##### Getters #####

  @Override
  public UUID getTurmaId() {
    return turmaId;
  }

  @Override
  public Integer getNumeroTurma() {
    return numeroTurma;
  }

  @Override
  public Integer getAnoLetivo() {
    return anoLetivo;
  }

  @Override
  public Set<AlunoInterface> getAlunos() {
    return Collections.unmodifiableSet(alunos);
  }

}
