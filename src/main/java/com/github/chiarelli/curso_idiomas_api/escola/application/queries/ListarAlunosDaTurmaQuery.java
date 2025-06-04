package com.github.chiarelli.curso_idiomas_api.escola.application.queries;

import java.util.Set;
import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;

import io.jkratz.mediator.core.Request;

public class ListarAlunosDaTurmaQuery implements Request<Set<AlunoInterface>> {

  private final UUID turmaId;

  public ListarAlunosDaTurmaQuery(UUID turmaId) {
    this.turmaId = turmaId;
  }

  public UUID getTurmaId() {
    return turmaId;
  }

}
