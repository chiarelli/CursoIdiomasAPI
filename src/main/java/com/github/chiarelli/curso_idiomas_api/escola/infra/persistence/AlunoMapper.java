package com.github.chiarelli.curso_idiomas_api.escola.infra.persistence;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;

@Component
public class AlunoMapper {

  public static Aluno toDomain(AlunoPersistence persistence) {
    var turmas = persistence.getTurmas().stream()
      .map(TurmaMapper::toDomain)
      .collect(Collectors.toSet());

    return new Aluno(
      persistence.getAlunoId(), 
      persistence.getNome(), 
      persistence.getCpf(), 
      persistence.getEmail(),
      turmas
    );
  }

  public static Aluno toDomainWithoutTurmas(AlunoPersistence persistence) {
    return new Aluno(
      persistence.getAlunoId(), 
      persistence.getNome(), 
      persistence.getCpf(), 
      persistence.getEmail(),
      Set.of()
    );
  }

}
