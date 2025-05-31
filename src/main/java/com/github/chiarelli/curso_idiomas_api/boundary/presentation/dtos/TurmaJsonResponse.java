package com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos;

import java.util.Set;

public class TurmaJsonResponse extends TurmaJsonRequest {

  public TurmaJsonResponse(Integer turmaId, Integer anoLetivo, Set<AlunoJsonResponse> alunos) {
    super(turmaId, anoLetivo, alunos);
  }

}
