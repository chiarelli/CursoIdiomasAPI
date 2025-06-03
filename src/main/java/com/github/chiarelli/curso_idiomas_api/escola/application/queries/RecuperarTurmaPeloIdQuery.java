package com.github.chiarelli.curso_idiomas_api.escola.application.queries;

import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import io.jkratz.mediator.core.Request;

public class RecuperarTurmaPeloIdQuery implements Request<TurmaInterface> {

  private final UUID turmaId;

  public RecuperarTurmaPeloIdQuery(UUID turmaId) {
    this.turmaId = turmaId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

}
