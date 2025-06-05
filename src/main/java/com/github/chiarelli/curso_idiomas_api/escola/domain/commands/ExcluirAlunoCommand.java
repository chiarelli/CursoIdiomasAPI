package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import io.jkratz.mediator.core.Request;

public class ExcluirAlunoCommand implements Request<Void> {

  private final UUID alunoId;

  public ExcluirAlunoCommand(UUID alunoId) {
    this.alunoId = alunoId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

}
