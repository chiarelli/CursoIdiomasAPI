package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import io.jkratz.mediator.core.Request;

public class DesmatricularAlunoTurmaCommand implements Request<Void> {
  
  private final UUID turmaId;
  private final UUID alunoId;

  public DesmatricularAlunoTurmaCommand(UUID turmaId, UUID alunoId) {
    this.turmaId = turmaId;
    this.alunoId = alunoId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

}
