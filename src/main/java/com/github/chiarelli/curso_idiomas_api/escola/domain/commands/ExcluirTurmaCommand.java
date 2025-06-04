package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import io.jkratz.mediator.core.Request;

public class ExcluirTurmaCommand implements Request<Void> {

  private final UUID turmaId;

  public ExcluirTurmaCommand(UUID turmaId) {
    this.turmaId = turmaId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

}
