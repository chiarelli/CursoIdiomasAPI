package com.github.chiarelli.curso_idiomas_api.escola.infra.persistence;

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

@Entity
@Table(name = "tb_turmas")
public class TurmaPersistence {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "turma_id", nullable = false, updatable = false, unique = true)
  private Integer turmaId;

  @Column(nullable = false)
  private Integer anoLetivo;

  @ManyToMany(mappedBy = "turmas", fetch = FetchType.LAZY)
  private Set<AlunoPersistence> alunos;

  public TurmaPersistence() {}

  public TurmaPersistence(UUID id, Integer turmaId, Integer anoLetivo) {
    this.id = id;
    this.turmaId = turmaId;
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

  public Integer getTurmaId() {
    return turmaId;
  }

  public void setTurmaId(Integer turmaId) {
    this.turmaId = turmaId;
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

}
