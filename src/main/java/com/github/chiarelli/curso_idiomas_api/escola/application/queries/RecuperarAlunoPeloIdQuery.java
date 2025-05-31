package com.github.chiarelli.curso_idiomas_api.escola.application.queries;

import java.util.UUID;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;

import io.jkratz.mediator.core.Request;

public class RecuperarAlunoPeloIdQuery implements Request<AlunoInterface> {

  private final UUID alunoId;
  
  public RecuperarAlunoPeloIdQuery(UUID alunoId) {
    if(alunoId == null) {
      throw new IllegalArgumentException("O id do aluno não pode ser nulo");
    }
    this.alunoId = alunoId;
  }

  public UUID getAlunoId() {
    return alunoId;
  }

}
