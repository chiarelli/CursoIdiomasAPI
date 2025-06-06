package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.AtualizarDadosTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaAtualizadaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.TurmaActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;

@Component
public class AtualizarDadosTurmaUsecase implements RequestHandler<AtualizarDadosTurmaCommand, TurmaInterface> {

  private final TurmaActions turmaActions;
  private final TurmaRepository turmaRepository;

  public AtualizarDadosTurmaUsecase(
    TurmaRepository turmaRepository,
    Mediator mediator,
    InstanceValidator validator
  ) {
    this.turmaRepository = turmaRepository;
    
    this.turmaActions = new TurmaActions(mediator, validator);
  }

  @Override
  @Transactional
  public TurmaInterface handle(AtualizarDadosTurmaCommand cmd) {
    if (turmaRepository.countByNumeroTurmaAndAnoLetivo(cmd.getNumeroTurma(), cmd.getAnoLetivo()) > 0) {
      throw new DomainException(
        "Turma com numero %s e ano letivo %s já cadastrada"
        .formatted(cmd.getNumeroTurma(), cmd.getAnoLetivo()));
    }

    var turmaP = turmaRepository.findById(cmd.getTurmaId())
      .orElseThrow(() -> new NotFoundException("Turma %s nao encontrada".formatted(cmd.getTurmaId())));

      turmaP.setNumeroTurma(cmd.getNumeroTurma());
      turmaP.setAnoLetivo(cmd.getAnoLetivo());

      turmaP = turmaRepository.save(turmaP);

      var turma = TurmaMapper.toDomain(turmaP);
      turmaActions.atualizarTurma(turma); // emite o evento de domínio 

      return turma;
  }

  @Component
  public static class TurmaAtualizadaHandler implements EventHandler<TurmaAtualizadaEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AtualizarDadosTurmaUsecase.class);
    @Override
    public void handle(TurmaAtualizadaEvent event) {
      logger.info("Turma {} atualizada", event.getTurma().getTurmaId());
    }
    
  }

}
