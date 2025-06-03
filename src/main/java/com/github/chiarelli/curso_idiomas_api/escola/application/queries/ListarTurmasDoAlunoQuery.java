package com.github.chiarelli.curso_idiomas_api.escola.application.queries;

import java.util.Set;
import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import io.jkratz.mediator.core.Request;

public class ListarTurmasDoAlunoQuery implements Request<Set<TurmaInterface>> {
  
  private final UUID alunoId;
  
  public ListarTurmasDoAlunoQuery(UUID alunoId) {
    this.alunoId = alunoId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }
  
}
