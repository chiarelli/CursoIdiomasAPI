package com.github.chiarelli.curso_idiomas_api.escola.domain.events;

import java.util.UUID;

import io.jkratz.mediator.core.Event;

public class AlunoExcluidoEvent implements Event {

  private final UUID alunoId;

  public AlunoExcluidoEvent(UUID alunoId) {
    this.alunoId = alunoId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

}
