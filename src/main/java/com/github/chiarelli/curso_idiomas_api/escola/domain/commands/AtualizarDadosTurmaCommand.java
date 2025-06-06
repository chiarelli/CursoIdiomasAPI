package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import io.jkratz.mediator.core.Request;

public class AtualizarDadosTurmaCommand implements Request<TurmaInterface> {

  private final UUID turmaId;
  private final int numeroTurma;
  private final int anoLetivo;

  public AtualizarDadosTurmaCommand(UUID turmaId, int numeroTurma, int anoLetivo) {
    this.turmaId = turmaId;
    this.numeroTurma = numeroTurma;
    this.anoLetivo = anoLetivo;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

  public int getNumeroTurma() {
    return numeroTurma;
  }

  public int getAnoLetivo() {
    return anoLetivo;
  }

}
