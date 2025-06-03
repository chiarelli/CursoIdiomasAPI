package com.github.chiarelli.curso_idiomas_api.escola.domain.events;

import java.util.UUID;

import io.jkratz.mediator.core.Event;

public class AlunoCadastradoEvent implements Event {
  
  private final UUID alunoId;
  
  public AlunoCadastradoEvent(UUID alunoId) {
    this.alunoId = alunoId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }
  
}
