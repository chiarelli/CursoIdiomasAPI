package com.github.chiarelli.curso_idiomas_api.escola.infra.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<TurmaPersistence, UUID> {

  @Query("SELECT COUNT(t) FROM TurmaPersistence t WHERE t.turmaId = :turmaId AND t.anoLetivo = :anoLetivo")
  long countByTurmaIdAndAnoLetivo(@Param("turmaId") Integer turmaId, @Param("anoLetivo") Integer anoLetivo);
  
}
