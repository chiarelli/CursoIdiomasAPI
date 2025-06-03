package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import io.jkratz.mediator.core.Request;

public class MatricularAlunoTurmaCommand implements Request<Void> {

  private final UUID turmaId;
  private final UUID alunoId;

  public MatricularAlunoTurmaCommand(UUID turmaId, UUID alunoId) {
    this.turmaId = turmaId;
    this.alunoId = alunoId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

}
