package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.Set;

public class TurmaJsonRequest extends NovaTurmaJsonRequest {

  private final Set<AlunoJsonResponse> alunos;

  public TurmaJsonRequest(Integer turmaId, Integer anoLetivo, Set<AlunoJsonResponse> alunos) {
    super(turmaId, anoLetivo);
    if (alunos == null) {
      alunos = Set.of();
    }
    this.alunos = alunos;
  }

   public Set<AlunoJsonResponse> getAlunos() {
    return alunos;
  }

}
