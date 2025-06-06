package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
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

  public Turma(UUID turmaId, Integer numeroTurma, Integer anoLetivo) {
    this.turmaId = turmaId;
    this.numeroTurma = numeroTurma;
    setAnoLetivo(anoLetivo);
  }

  public void setAnoLetivo(Integer anoLetivo) {
    this.anoLetivo = anoLetivo;
  }
  
  public void addAluno(Aluno aluno) {
    if(alunos.size() > LOTACAO_MAX) {
      throw new DomainException("Lotação da turma excedida");
    }
    alunos.add(aluno);
  }
  
  public void removeAluno(Aluno aluno) {
    alunos.remove(aluno);
  }

  void clear() {
    this.alunos.clear();
    this.turmaId = null;
    this.anoLetivo = null;
    this.numeroTurma = null;
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

  // ##### Other #####

  @Override
  public String toString() {
    return "Turma [turmaId=" + turmaId + ", numeroTurma=" + numeroTurma + ", anoLetivo=" + anoLetivo + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(turmaId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Turma other = (Turma) obj;
    if (turmaId == null) {
      if (other.turmaId != null)
        return false;
    } else if (!turmaId.equals(other.turmaId))
      return false;
    return true;
  }

}
