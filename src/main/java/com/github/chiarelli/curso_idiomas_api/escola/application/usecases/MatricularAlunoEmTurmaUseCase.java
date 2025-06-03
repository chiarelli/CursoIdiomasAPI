package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarAlunoUseCase.AlunoCadastradoHandler;
import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.MatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoMatriculadoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class MatricularAlunoEmTurmaUseCase implements RequestHandler<MatricularAlunoTurmaCommand, Void> {

  private final TurmaRepository turmaRepository;
  private final AlunoRepository alunoRepository;
  private final Mediator mediator;

  public MatricularAlunoEmTurmaUseCase(
    TurmaRepository turmaRepository,
    AlunoRepository alunoRepository,
    Mediator mediator
  ) {
    this.turmaRepository = turmaRepository;
    this.alunoRepository = alunoRepository;
    this.mediator = mediator;
  }

  @Override
  @Transactional
  public Void handle(MatricularAlunoTurmaCommand cmd) {
    var turmaP = turmaRepository.findById(cmd.getTurmaId())
      .orElseThrow(() -> new NotFoundException("Turma %s nao encontrada".formatted(cmd.getTurmaId())));

    turmaP.getAlunos().forEach(al -> {
      if (al.getAlunoId().equals(cmd.getAlunoId())) {
        throw new DomainException("Aluno %s ja matriculado na turma %s".formatted(cmd.getAlunoId(), cmd.getTurmaId()));
      }
    });

    var alunoP = alunoRepository.findById(cmd.getAlunoId())
      .orElseThrow(() -> new NotFoundException("Aluno %s nao encontrado".formatted(cmd.getAlunoId())));

    var turma = TurmaMapper.toDomain(turmaP);
    var aluno = AlunoMapper.toDomain(alunoP);
    
    // Salvar as alterações
    alunoP.getTurmas().add(turmaP);
    alunoRepository.save(alunoP);
    
    turma.matricularAluno(mediator, aluno); // emite o evento de domínio

    return null;
  }

  @Component
  public static class MatricularAlunoHandler implements EventHandler<AlunoMatriculadoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AlunoCadastradoHandler.class);

    @Override
    public void handle(AlunoMatriculadoEvent evt) {
      logger.info("Aluno %s matriculado na turma %s ".formatted(evt.getAlunoId(), evt.getTurmaId()));
    }
  
    
  }

}
