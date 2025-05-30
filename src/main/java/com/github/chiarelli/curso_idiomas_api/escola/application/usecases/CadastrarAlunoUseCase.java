package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoRepository;

import io.jkratz.mediator.core.RequestHandler;

@Component
public class CadastrarAlunoUseCase implements RequestHandler<RegistrarNovoAlunoCommand, AlunoInterface> {

  @Autowired InstanceValidator validator;
  @Autowired AlunoRepository repository;

  @Override
  public AlunoInterface handle(RegistrarNovoAlunoCommand cmd) {
    // Mapeia o payload do comando para o aggregate
    var aggregate = RegistrarNovoAlunoCommand.toDomain(UUID.randomUUID(), cmd);
    // Valida o aggregate
    validator.validate(aggregate);
    // Mapeia o aggregate para o persistence
    var persistence = new AlunoPersistence(
      aggregate.getAlunoId(), 
      aggregate.getNome(), 
      aggregate.getCpf(), 
      aggregate.getEmail()
    );
    // Salva o persistence
    persistence = repository.save(persistence);
    // Mapeia o persistence para o aggregate
    aggregate = new Aluno(
      persistence.getAlunoId(), 
      persistence.getNome(), 
      persistence.getCpf(), 
      persistence.getEmail()
    );
    
    return aggregate;
  }

}
