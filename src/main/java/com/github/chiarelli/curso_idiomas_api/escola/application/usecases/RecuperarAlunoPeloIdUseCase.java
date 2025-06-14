package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarAlunoPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class RecuperarAlunoPeloIdUseCase implements RequestHandler<RecuperarAlunoPeloIdQuery, AlunoInterface> {

  @Autowired AlunoRepository repository;

  @Override
  @Transactional(readOnly = true)
  public AlunoInterface handle(RecuperarAlunoPeloIdQuery query) {
    var alunoId = query.getAlunoId();
    var opPersistence = repository.findById(alunoId);

    if (opPersistence.isEmpty()) {
      throw new ResourceNotFoundException("aluno id " + alunoId.toString() + " not exists.");
    }

    var persistence = opPersistence.get();

    // Mapeia o persistence para o aggregate
    var aggregate = new Aluno(
      persistence.getAlunoId(), 
      persistence.getNome(), 
      persistence.getCpf(), 
      persistence.getEmail(),
      persistence.getTurmas().stream().map(TurmaMapper::toDomain).collect(Collectors.toSet())
    );

    return aggregate;
  }

}
