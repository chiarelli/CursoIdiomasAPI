package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarAlunoUseCase.AlunoCadastradoHandler;
import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.DesmatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoRemovidoTurmaEvent;
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
public class DesmatricularAlunoEmTurmaUseCase implements RequestHandler<DesmatricularAlunoTurmaCommand, Void> {

  private final TurmaRepository turmaRepository;
  private final AlunoRepository alunoRepository;
  private final Mediator mediator;

  public DesmatricularAlunoEmTurmaUseCase(
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
  public Void handle(DesmatricularAlunoTurmaCommand cmd) {
    var alunoP = alunoRepository.findById(cmd.getAlunoId())
      .orElseThrow(() -> new NotFoundException("Aluno %s nao encontrado".formatted(cmd.getAlunoId())));
    
    var turmaP = turmaRepository.findById(cmd.getTurmaId())
      .orElseThrow(() -> new NotFoundException("Turma %s nao encontrada".formatted(cmd.getTurmaId())));

    if(!alunoP.getTurmas().contains(turmaP)) 
      throw new DomainException("Aluno %s nao matriculado na turma %s".formatted(cmd.getAlunoId(), cmd.getTurmaId())); //alunoP.getTurmas().contains(turmaP)

    var turma = TurmaMapper.toDomain(turmaP);
    var aluno = AlunoMapper.toDomain(alunoP);
    
    // Salvar as alterações
    alunoP.getTurmas().remove(turmaP);
    alunoRepository.save(alunoP);
    
    turma.desmatricularAluno(mediator, aluno); // emite o evento de domínio

    return null;
  }

  @Component
  public static class AlunoRemovidoTurmoHandler implements EventHandler<AlunoRemovidoTurmaEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AlunoCadastradoHandler.class);

    @Override
    public void handle(AlunoRemovidoTurmaEvent evt) {
      logger.info("Aluno %s removido da turma %s ".formatted(evt.getAlunoId(), evt.getTurmaId()));
    }
  
    
  }

}
