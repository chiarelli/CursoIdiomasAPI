package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.ListarAlunosDaTurmaQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class ListarAlunosDaTurmaUsecase implements RequestHandler<ListarAlunosDaTurmaQuery, Set<AlunoInterface>> {

  private final TurmaRepository repository;

  public ListarAlunosDaTurmaUsecase(TurmaRepository turmaRepository) {
    this.repository = turmaRepository;
  }

  @Override
  public Set<AlunoInterface> handle(ListarAlunosDaTurmaQuery query) {
    var turma = repository.findById(query.getTurmaId())
      .orElseThrow(() -> new NotFoundException("Turma %s nao encontrada".formatted(query.getTurmaId())));

    return turma.getAlunos().stream().map(AlunoMapper::toDomain).collect(Collectors.toSet());
  }

}
