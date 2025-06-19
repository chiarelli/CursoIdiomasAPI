package com.github.chiarelli.curso_idiomas_api.escola.infra.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaPersistence, UUID> {

  @Query("SELECT COUNT(t) FROM TurmaPersistence t WHERE t.numeroTurma = :numeroTurma AND t.anoLetivo = :anoLetivo")
  long countByNumeroTurmaAndAnoLetivo(@Param("numeroTurma") Integer numeroTurma, @Param("anoLetivo") Integer anoLetivo);

  @Query("SELECT t FROM TurmaPersistence t WHERE t.numeroTurma = :numeroTurma AND t.anoLetivo = :anoLetivo")
  Optional<TurmaPersistence> findByNumeroTurmaAndAnoLetivo(Integer numeroTurma, Integer anoLetivo);
  
}
