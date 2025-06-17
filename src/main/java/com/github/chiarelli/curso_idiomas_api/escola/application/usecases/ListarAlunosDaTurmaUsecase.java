package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.ListarAlunosDaTurmaQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class ListarAlunosDaTurmaUsecase implements RequestHandler<ListarAlunosDaTurmaQuery, Set<AlunoInterface>> {

  private final TurmaRepository repository;

  public ListarAlunosDaTurmaUsecase(TurmaRepository turmaRepository) {
    this.repository = turmaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Set<AlunoInterface> handle(ListarAlunosDaTurmaQuery query) {
    var turma = repository.findById(query.getTurmaId())
      .orElseThrow(() -> new ResourceNotFoundException("Turma %s nao encontrada".formatted(query.getTurmaId())));

    return turma.getAlunos().stream()
        .map(AlunoMapper::toDomain)
        .sorted(Comparator.comparing(Aluno::getNome))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

}
