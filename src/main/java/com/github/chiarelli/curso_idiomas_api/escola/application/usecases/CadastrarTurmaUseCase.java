package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Turma;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaRepository;

import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class CadastrarTurmaUseCase implements RequestHandler<CadastrarNovaTurmaCommand, TurmaInterface> {

  @Autowired InstanceValidator validator;
  @Autowired TurmaRepository repository;

  @Override
  @Transactional
  public TurmaInterface handle(CadastrarNovaTurmaCommand cmd) {
    var domain = CadastrarNovaTurmaCommand.toDomain(UUID.randomUUID(), cmd);
    validator.validate(domain);

    if (repository.countByTurmaIdAndAnoLetivo(domain.getNumeroTurma(), domain.getAnoLetivo()) > 0) {
      throw new DomainException("Turma com id " + domain.getNumeroTurma() + " já cadastrada");
    }

    var persistence = new TurmaPersistence(domain.getTurmaId(), domain.getNumeroTurma(), domain.getAnoLetivo());

    persistence = repository.save(persistence);

    return new Turma(persistence.getId(), persistence.getTurmaId(), persistence.getAnoLetivo());
  }

}
