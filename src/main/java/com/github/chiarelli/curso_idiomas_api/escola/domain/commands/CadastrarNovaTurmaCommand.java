package com.github.chiarelli.curso_idiomas_api.escola.domain.commands;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Turma;

import io.jkratz.mediator.core.Request;

public class CadastrarNovaTurmaCommand implements Request<TurmaInterface> {
  
  private final Integer turmaId;
  private final Integer anoLetivo;

  public CadastrarNovaTurmaCommand(Integer turmaId, Integer anoLetivo) {
    this.turmaId = turmaId;
    this.anoLetivo = anoLetivo;
  }

  public Integer getTurmaId() {
    return turmaId;
  }

  public Integer getAnoLetivo() {
    return anoLetivo;
  }

  public static Turma toDomain(CadastrarNovaTurmaCommand cmd) {
    return new Turma(cmd.getTurmaId(), cmd.getAnoLetivo());
    
  }
}
