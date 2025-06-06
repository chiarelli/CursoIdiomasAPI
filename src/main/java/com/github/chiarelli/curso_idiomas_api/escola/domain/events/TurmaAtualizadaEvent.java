package com.github.chiarelli.curso_idiomas_api.escola.domain.events;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import io.jkratz.mediator.core.Event;

public class TurmaAtualizadaEvent implements Event {

  private final TurmaInterface turma;

  public TurmaAtualizadaEvent(TurmaInterface turma) {
    this.turma = turma;
  }

  public TurmaInterface getTurma() {
    return turma;
  }

}
