package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.DesmatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoExcluidoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.AlunoActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;

@Component
public class ExcluirAlunoUseCase implements RequestHandler<ExcluirAlunoCommand, Void> {

  private final AlunoRepository alunoRepository;
  private final DesmatricularAlunoEmTurmaUseCase desmatricularAluno;
  private final AlunoActions alunoActions;

  public ExcluirAlunoUseCase(
    AlunoRepository alunoRepository,
    DesmatricularAlunoEmTurmaUseCase desmatricularAluno,
    Mediator mediator,
    InstanceValidator validator
  ) {
    this.alunoRepository = alunoRepository;
    this.desmatricularAluno = desmatricularAluno;

    this.alunoActions = new AlunoActions(mediator, validator);
  }

  @Override
  @Transactional
  public Void handle(ExcluirAlunoCommand cmd) {
    var result = alunoRepository.findById(cmd.getAlunoId());
    
    if(result.isEmpty()) {
      return null; // não faz nada, pois método é idempotente
    }

    var alunoPer = result.get();
    var aluno = AlunoMapper.toDomain(alunoPer);

    if(!aluno.canBeDeleted()) {
      throw new DomainException("Aluno %s nao pode ser excluído".formatted(aluno.getAlunoId()));
    }

    alunoPer.getTurmas().forEach(t -> {
      var turma = TurmaMapper.toDomain(t);

      desmatricularAluno.handle(
        new DesmatricularAlunoTurmaCommand(turma.getTurmaId(), aluno.getAlunoId())
      );
      
    });

    alunoRepository.delete(alunoPer); // exclui o aluno no banco de dados
    alunoActions.excluirAluno(aluno); // emite o evento de domínio
    
    return null;
  }

  @Component
  public static class ExcluirAlunoHandler implements EventHandler<AlunoExcluidoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ExcluirAlunoUseCase.class);

    @Override
    public void handle(AlunoExcluidoEvent event) {
      logger.info("Aluno %s excluido".formatted(event.getAlunoId()));
    }

  }

}
