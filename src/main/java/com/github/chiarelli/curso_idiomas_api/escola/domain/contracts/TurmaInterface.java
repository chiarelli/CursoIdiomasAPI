package com.github.chiarelli.curso_idiomas_api.escola.domain.contracts;

import java.util.Set;
import java.util.UUID;

public interface TurmaInterface {

  UUID getTurmaId();

  Integer getNumeroTurma();

  Integer getAnoLetivo();

  Set<AlunoInterface> getAlunos();

}