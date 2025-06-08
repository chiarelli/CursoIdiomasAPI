package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.DesmatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoDesmatriculadoTurmaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.TurmaActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;

@Component
public class DesmatricularAlunoEmTurmaUseCase implements RequestHandler<DesmatricularAlunoTurmaCommand, Void> {

  private final TurmaRepository turmaRepository;
  private final AlunoRepository alunoRepository;
  private final TurmaActions actions;

  public DesmatricularAlunoEmTurmaUseCase(
    TurmaRepository turmaRepository,
    AlunoRepository alunoRepository,
    Mediator mediator,
    InstanceValidator validator
  ) {
    this.turmaRepository = turmaRepository;
    this.alunoRepository = alunoRepository;

    this.actions = new TurmaActions(mediator, validator);
  }

  @Override
  @Transactional
  public Void handle(DesmatricularAlunoTurmaCommand cmd) {
    var alunoP = alunoRepository.findById(cmd.getAlunoId())
      .orElseThrow(() -> new ResourceNotFoundException("Aluno %s nao encontrado".formatted(cmd.getAlunoId())));
    
    var turmaP = turmaRepository.findById(cmd.getTurmaId())
      .orElseThrow(() -> new ResourceNotFoundException("Turma %s nao encontrada".formatted(cmd.getTurmaId())));

    if(!alunoP.getTurmas().contains(turmaP)) 
      throw new DomainException("Aluno %s nao matriculado na turma %s".formatted(cmd.getAlunoId(), cmd.getTurmaId())); //alunoP.getTurmas().contains(turmaP)

    var turma = TurmaMapper.toDomain(turmaP);
    var aluno = AlunoMapper.toDomain(alunoP);
    
    actions.desmatricularAluno(turma, aluno); // emite o evento de domínio
    
    // Salvar as alterações
    alunoP.getTurmas().remove(turmaP);
    alunoRepository.save(alunoP);

    return null;
  }

  @Component
  public static class AlunoRemovidoTurmoHandler implements EventHandler<AlunoDesmatriculadoTurmaEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DesmatricularAlunoEmTurmaUseCase.class);

    @Override
    public void handle(AlunoDesmatriculadoTurmaEvent evt) {
      logger.info("Aluno %s desmatriculado da turma %s ".formatted(evt.getAlunoId(), evt.getTurmaId()));
    }
  
    
  }

}
