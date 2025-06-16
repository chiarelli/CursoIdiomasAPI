package com.github.chiarelli.curso_idiomas_api.escola.infra.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoPersistence, UUID> {

  @Query("""
    SELECT a
    FROM AlunoPersistence a
    WHERE a.cpf = :cpf
      OR a.email = :email
    """)
  Optional<AlunoPersistence> findByCpfOrEmail(String cpf, String email);

}
