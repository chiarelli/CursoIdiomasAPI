package com.github.chiarelli.curso_idiomas_api.escola.domain.events;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;

import io.jkratz.mediator.core.Event;

public class AlunoAtualizadoEvent implements Event {

  private final AlunoInterface aluno;

  public AlunoAtualizadoEvent(AlunoInterface aluno) {
    this.aluno = aluno;
  }

  public AlunoInterface getAluno() {
    return aluno;
  }

}
