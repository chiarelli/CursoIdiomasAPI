package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.PageListarTurmasQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class ListarTurmasUsecase implements RequestHandler<PageListarTurmasQuery, Page<TurmaInterface>> {

  private final TurmaRepository repository;

  public ListarTurmasUsecase(TurmaRepository turmaRepository) {
    this.repository = turmaRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TurmaInterface> handle(PageListarTurmasQuery query) {
    var pageable = PageRequest.of(query.getPage() - 1, query.getSize());
    var turmaPer = repository.findAll(pageable);

    return new PageImpl<>(
      turmaPer.getContent().stream().map(TurmaMapper::toDomain).collect(Collectors.toList()), 
      pageable, 
      turmaPer.getTotalElements()
    );
  }


}
