package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarTurmaPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class RecuperarTurmaPeloIdUsecase implements RequestHandler<RecuperarTurmaPeloIdQuery, TurmaInterface> {

  private final TurmaRepository repository;

  public RecuperarTurmaPeloIdUsecase(TurmaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public TurmaInterface handle(RecuperarTurmaPeloIdQuery query) {
    var turma = repository.findById(query.getTurmaId())
      .orElseThrow(() -> new ResourceNotFoundException("Turma %s nao encontrada".formatted(query.getTurmaId())));

    return TurmaMapper.toDomain(turma);
  }

}
