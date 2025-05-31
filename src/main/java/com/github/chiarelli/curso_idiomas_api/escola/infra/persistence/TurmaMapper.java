package com.github.chiarelli.curso_idiomas_api.escola.infra.persistence;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Turma;

@Component
public class TurmaMapper {

  public static Turma toDomain(TurmaPersistence persistence) {
    var alunos = persistence.getAlunos().stream()
      .map(AlunoMapper::toDomain)
      .collect(Collectors.toSet());    
        
    return new Turma(persistence.getTurmaId(), persistence.getAnoLetivo(), alunos);
  }

}
