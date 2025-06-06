package com.github.chiarelli.curso_idiomas_api.escola.infra.jpa;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "tb_alunos")
public class AlunoPersistence {
  
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID alunoId;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false, updatable = false,  unique = true, length = 11)
  private String cpf;
  
  @Column(nullable = false, unique = true)
  @Email
  private String email;
  
  @ManyToAny(fetch = FetchType.LAZY)
  @JoinTable(
    name = "turmas_relationships_alunos",
    joinColumns = @JoinColumn(name = "aluno_id"),
    inverseJoinColumns = @JoinColumn(name = "turma_id")
  )
  private Set<TurmaPersistence> turmas;

  public AlunoPersistence() {}

  public AlunoPersistence(UUID alunoId, String nome, String cpf, String email) {
    this.alunoId = alunoId;
    this.nome = nome;
    this.cpf = cpf;
    this.email = email;
  }

  public AlunoPersistence(UUID alunoId, String nome, String cpf, String email, Set<TurmaPersistence> turmas) {
    this(alunoId, nome, cpf, email);
    this.turmas = turmas;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

  public void setAlunoId(UUID alunoId) {
    this.alunoId = alunoId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<TurmaPersistence> getTurmas() {
    if (turmas == null) {
        turmas = new HashSet<>();
    }
    return turmas;
  }

  public void setTurmas(Set<TurmaPersistence> turmas) {
    if (Objects.nonNull(turmas)) {
      this.turmas = turmas;
    }
  }

  @Override
  public String toString() {
    return "AlunoEntity [alunoId=" + alunoId + ", nome=" + nome + ", cpf=" + cpf + ", email=" + email + "]";
  }

  @PrePersist
  void onCreate() {
    cleanCpf();
  }

  @PreUpdate
  void onUpdate() {
    cleanCpf();
  }

  private void cleanCpf() {
    this.cpf = this.cpf.replaceAll("[^0-9]", "");
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof AlunoPersistence)) return false;
      AlunoPersistence aluno = (AlunoPersistence) o;
      return alunoId != null && alunoId.equals(aluno.alunoId);
  }

  @Override
  public int hashCode() {
      return Objects.hashCode(alunoId);
  }

}
