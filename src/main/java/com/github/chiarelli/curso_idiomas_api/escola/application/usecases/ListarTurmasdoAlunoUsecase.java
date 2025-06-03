package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.ListarTurmasDoAlunoQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class ListarTurmasdoAlunoUsecase implements RequestHandler<ListarTurmasDoAlunoQuery, Set<TurmaInterface>> {

  private final AlunoRepository repository;

  public ListarTurmasdoAlunoUsecase(AlunoRepository repository) {
    this.repository = repository;
  }

  @Override
  public Set<TurmaInterface> handle(ListarTurmasDoAlunoQuery query) {
    var aluno = repository.findById(query.getAlunoId())
      .orElseThrow(() -> new NotFoundException("Aluno %s nao encontrado".formatted(query.getAlunoId())));
      
    return aluno.getTurmas().stream().map(TurmaMapper::toDomain).collect(Collectors.toSet());
  }

}
