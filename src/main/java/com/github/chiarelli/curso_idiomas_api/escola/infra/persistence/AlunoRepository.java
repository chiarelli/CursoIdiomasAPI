package com.github.chiarelli.curso_idiomas_api.escola.infra.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<AlunoEntity, UUID> {

}
