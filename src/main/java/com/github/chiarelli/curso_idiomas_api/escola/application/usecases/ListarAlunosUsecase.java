package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.PageListarAlunosQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class ListarAlunosUsecase implements RequestHandler<PageListarAlunosQuery, Page<AlunoInterface>> {

  private final AlunoRepository repository;

  public ListarAlunosUsecase(AlunoRepository alunoRepository) {
    this.repository = alunoRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<AlunoInterface> handle(PageListarAlunosQuery query) {
    Sort sort = Sort.by(Sort.Direction.ASC, "nome");
    var pageable = PageRequest.of(query.getPage() - 1, query.getSize(), sort);
    Page<AlunoPersistence> page = repository.findAll(pageable);

    return new PageImpl<>(
      page.getContent().stream().map(AlunoMapper::toDomain).collect(Collectors.toList()), 
      pageable, 
      page.getTotalElements()
    );
  }

}
