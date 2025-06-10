package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaExcluidaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.TurmaActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;

@Component
public class ExcluirTurmaUseCase implements RequestHandler<ExcluirTurmaCommand, Void> {

  private final TurmaRepository repository;
  private final TurmaActions turmaActions;

  public ExcluirTurmaUseCase(
    TurmaRepository repository,
    Mediator mediator,
    InstanceValidator validator
  ) {
    this.repository = repository;

    this.turmaActions = new TurmaActions(mediator, validator);
  }

  @Override
  @Transactional
  public Void handle(ExcluirTurmaCommand cmd) {
    var result = repository.findById(cmd.getTurmaId());
    
    if(result.isEmpty()) {
      return null; // não faz nada, pois método é idempotente
    }
    
    var turmaPer = result.get();
            
    repository.delete(turmaPer); // exclui a turma no banco de dados
    var turma = TurmaMapper.toDomain(turmaPer);
    
    turmaActions.excluirTurma(turma); // emite o evento de domínio
    return null;
  }

  @Component
  public static class ExcluirTurmaHandler implements EventHandler<TurmaExcluidaEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcluirTurmaUseCase.class);

    @Override
    public void handle(TurmaExcluidaEvent event) {
      logger.info("Turma excluida: " + event.getTurmaId());
    }
  }

}
