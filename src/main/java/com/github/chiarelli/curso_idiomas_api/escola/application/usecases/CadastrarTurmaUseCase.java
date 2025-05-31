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

@Component
public class CadastrarTurmaUseCase implements RequestHandler<CadastrarNovaTurmaCommand, TurmaInterface> {

  @Autowired InstanceValidator validator;
  @Autowired TurmaRepository repository;

  @Override
  public TurmaInterface handle(CadastrarNovaTurmaCommand cmd) {
    var domain = CadastrarNovaTurmaCommand.toDomain(cmd);
    validator.validate(domain);

    if (repository.countByTurmaIdAndAnoLetivo(domain.getTurmaId(), domain.getAnoLetivo()) > 0) {
      throw new DomainException("Turma com id " + domain.getTurmaId() + " já cadastrada para o ano letivo " + domain.getAnoLetivo());
    }

    var persistence = new TurmaPersistence(UUID.randomUUID(), domain.getTurmaId(), domain.getAnoLetivo());

    persistence = repository.save(persistence);

    return new Turma(persistence.getTurmaId(), persistence.getAnoLetivo());
  }

}
