package com.github.chiarelli.curso_idiomas_api.escola.domain.contracts;

import java.util.Set;

public interface TurmaInterface {

  Integer getTurmaId();

  Integer getAnoLetivo();

  Set<AlunoInterface> getAlunos();

}