package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarAlunoUseCase.AlunoCadastradoHandler;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaExcluidaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class ExcluirTurmaUseCase implements RequestHandler<ExcluirTurmaCommand, Void> {

  private final TurmaRepository repository;
  private final Mediator mediator;

  public ExcluirTurmaUseCase(
    TurmaRepository repository,
    Mediator mediator
  ) {
    this.mediator = mediator;
    this.repository = repository;
  }

  @Override
  @Transactional
  public Void handle(ExcluirTurmaCommand cmd) {
    var turmaPer = repository.findById(cmd.getTurmaId())
        .orElseThrow(() -> new NotFoundException("Turma %s nao encontrada".formatted(cmd.getTurmaId())));
            
    repository.delete(turmaPer); // exclui a turma no banco de dados
    var turma = TurmaMapper.toDomain(turmaPer);
    
    turma.excluirTurma(mediator); // emite o evento de domínio
    return null;
  }

  @Component
  public static class ExcluirTurmaHandler implements EventHandler<TurmaExcluidaEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(AlunoCadastradoHandler.class);

    @Override
    public void handle(TurmaExcluidaEvent event) {
      logger.info("Turma excluida: " + event.getTurmaId());
    }
  }

}
