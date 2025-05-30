package com.github.chiarelli.curso_idiomas_api.escola.domain.contracts;

import java.util.Set;
import java.util.UUID;

public interface AlunoInterface {

  UUID getAlunoId();

  String getNome();

  String getCpf();

  String getEmail();

  Set<TurmaInterface> getTurmas();

}