package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import java.util.HashSet;
import java.util.Set;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Turma implements TurmaInterface {

  @Min(1)
  @NotNull
  private Integer turmaId;
  
  @NotNull
  @Min(1900)
  @Max(2399)
  private Integer anoLetivo;

  private Set<Aluno> alunos = new HashSet<>();

  public Turma(Integer turmaId, Integer anoLetivo, Set<Aluno> alunos) {
    this.turmaId = turmaId;
    alterarAnoLetivo(anoLetivo);

    for(Aluno aluno : alunos) {
      setAluno(aluno);
      aluno.setTurmaMatriculada(this);
    }
  }

  public Turma(Integer turmaId, Integer anoLetivo) {
    this.turmaId = turmaId;
    alterarAnoLetivo(anoLetivo);
  }

  void setAluno(Aluno aluno) {
    throw new IllegalArgumentException("implement method adicionarAluno");
  }

  // ##### Actions #####

  public void alterarAnoLetivo(Integer anoLetivo) {
    this.anoLetivo = anoLetivo;
  }

  public void matricularAluno(Aluno aluno) {
    throw new IllegalArgumentException("implement method adicionarAluno");
  }

  public void removerAluno(Aluno aluno) {
    throw new IllegalArgumentException("implement method removerAluno");
  }

  // ##### Getters #####

  @Override
  public Integer getTurmaId() {
    return turmaId;
  }

  @Override
  public Integer getAnoLetivo() {
    return anoLetivo;
  }

  @Override
  public Set<AlunoInterface> getAlunos() {
    return new HashSet<>(alunos);
  }

}
