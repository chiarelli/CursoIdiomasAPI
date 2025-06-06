package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaCadastradaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.TurmaActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;

@Component
public class CadastrarTurmaUseCase implements RequestHandler<CadastrarNovaTurmaCommand, TurmaInterface> {

  private final TurmaRepository repository;
  private final TurmaActions turmaActions;

  public CadastrarTurmaUseCase(
    TurmaRepository repository,
    InstanceValidator validator,
    Mediator mediator 
  ) {
    this.repository = repository;

    this.turmaActions = new TurmaActions(mediator, validator);
  }

  @Override
  @Transactional
  public TurmaInterface handle(CadastrarNovaTurmaCommand cmd) {
    var domain = CadastrarNovaTurmaCommand.toDomain(UUID.randomUUID(), cmd);

    if (repository.countByNumeroTurmaAndAnoLetivo(domain.getNumeroTurma(), domain.getAnoLetivo()) > 0) {
      throw new DomainException(
        "Turma com numero %s e ano letivo %s já cadastrada"
        .formatted(domain.getNumeroTurma(), domain.getAnoLetivo())
      );
    }

    var persistence = new TurmaPersistence(domain.getTurmaId(), domain.getNumeroTurma(), domain.getAnoLetivo());
    // persiste a turma
    persistence = repository.save(persistence);

    var turmaCreated = TurmaMapper.toDomain(persistence);
    // Emitir evento de domínio
    turmaActions.criarTurma(turmaCreated);

    return turmaCreated;
  }

  @Component
  public static class TurmaCadastradaHandler implements EventHandler<TurmaCadastradaEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CadastrarTurmaUseCase.class);

    @Override
    public void handle(TurmaCadastradaEvent event) {
      logger.info("Turma cadastrada: " + event.getTurmaId());
    }
    
  }

}
