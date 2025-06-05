package com.github.chiarelli.curso_idiomas_api.escola.domain.events;

import java.util.UUID;

import io.jkratz.mediator.core.Event;

public class TurmaCadastradaEvent implements Event {

  private final UUID turmaId;

  public TurmaCadastradaEvent(UUID turmaId) {
    this.turmaId = turmaId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

}
