package com.github.chiarelli.curso_idiomas_api.escola.domain.events;

import java.util.UUID;

import io.jkratz.mediator.core.Event;

public class AlunoMatriculadoEvent implements Event {

  private final UUID alunoId;
  private final UUID turmaId;

  public AlunoMatriculadoEvent(UUID alunoId, UUID turmaId) {
    this.alunoId = alunoId;
    this.turmaId = turmaId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }
  
}
