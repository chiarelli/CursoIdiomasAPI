package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.MatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoMatriculadoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.TurmaActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class MatricularAlunoEmTurmaUseCase implements RequestHandler<MatricularAlunoTurmaCommand, Void> {

  private final TurmaRepository turmaRepository;
  private final AlunoRepository alunoRepository;
  private final TurmaActions actions;

  public MatricularAlunoEmTurmaUseCase(
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
  public Void handle(MatricularAlunoTurmaCommand cmd) {
    var turmaP = turmaRepository.findById(cmd.getTurmaId())
      .orElseThrow(() -> new ResourceNotFoundException("Turma %s nao encontrada".formatted(cmd.getTurmaId())));

    turmaP.getAlunos().forEach(al -> {
      if (al.getAlunoId().equals(cmd.getAlunoId())) {
        throw new DomainException("Aluno %s ja matriculado na turma %s".formatted(cmd.getAlunoId(), cmd.getTurmaId()));
      }
    });

    var alunoP = alunoRepository.findById(cmd.getAlunoId())
      .orElseThrow(() -> new ResourceNotFoundException("Aluno %s nao encontrado".formatted(cmd.getAlunoId())));

    var turma = TurmaMapper.toDomain(turmaP);
    var aluno = AlunoMapper.toDomain(alunoP);
    
    // Salvar as alterações
    alunoP.getTurmas().add(turmaP);
    alunoRepository.save(alunoP);
    
    actions.matricularAluno(turma, aluno); // emite o evento de domínio

    return null;
  }

  @Component
  public static class MatricularAlunoHandler implements EventHandler<AlunoMatriculadoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MatricularAlunoEmTurmaUseCase.class);

    @Override
    public void handle(AlunoMatriculadoEvent evt) {
      logger.info("Aluno %s matriculado na turma %s ".formatted(evt.getAlunoId(), evt.getTurmaId()));
    }
  
    
  }

}
