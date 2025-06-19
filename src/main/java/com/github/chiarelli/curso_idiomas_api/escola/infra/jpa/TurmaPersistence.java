package com.github.chiarelli.curso_idiomas_api.escola.infra.jpa;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
  name = "tb_turmas",
  uniqueConstraints = @UniqueConstraint(columnNames = {"numero_turma", "ano_letivo"})
)
public class TurmaPersistence {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "numero_turma", nullable = false)
  private Integer numeroTurma;

  @Column(name = "ano_letivo", nullable = false)
  private Integer anoLetivo;

  @ManyToMany(mappedBy = "turmas", fetch = FetchType.LAZY)
  private Set<AlunoPersistence> alunos;

  public TurmaPersistence() {}

  public TurmaPersistence(UUID id, Integer numeroTurma, Integer anoLetivo) {
    this.id = id;
    this.numeroTurma = numeroTurma;
    this.anoLetivo = anoLetivo;
  }

  public TurmaPersistence(UUID id, Integer turmaId, Integer anoLetivo, Set<AlunoPersistence> alunos) {
    this(id, turmaId, anoLetivo);
    if(alunos == null) {
      alunos = new HashSet<>();
    }
    this.alunos = alunos;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Integer getNumeroTurma() {
    return numeroTurma;
  }

  public void setNumeroTurma(Integer numeroTurma) {
    this.numeroTurma = numeroTurma;
  }

  public Integer getAnoLetivo() {
    return anoLetivo;
  }

  public void setAnoLetivo(Integer anoLetivo) {
    this.anoLetivo = anoLetivo;
  }

  public Set<AlunoPersistence> getAlunos() {
    if (alunos == null) {
        alunos = new HashSet<>();
    }
    return alunos;
  }

  public void setAlunos(Set<AlunoPersistence> alunos) {
    if(Objects.nonNull(alunos)) {
      this.alunos = alunos;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TurmaPersistence)) return false;
    TurmaPersistence turma = (TurmaPersistence) o;
    return id != null && id.equals(turma.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

}
