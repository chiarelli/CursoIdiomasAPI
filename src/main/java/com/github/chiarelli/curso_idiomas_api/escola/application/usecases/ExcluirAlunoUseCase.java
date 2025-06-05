package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoExcluidoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class ExcluirAlunoUseCase implements RequestHandler<ExcluirAlunoCommand, Void> {

  private final AlunoRepository alunoRepository;
  private final TurmaRepository turmaRepository;
  private final Mediator mediator;

  public ExcluirAlunoUseCase(
    AlunoRepository alunoRepository,
    TurmaRepository turmaRepository,
    Mediator mediator
  ) {
    this.alunoRepository = alunoRepository;
    this.turmaRepository = turmaRepository;
    this.mediator = mediator;
  }

  @Override
  @Transactional
  public Void handle(ExcluirAlunoCommand cmd) {
    var alunoPer = alunoRepository.findById(cmd.getAlunoId())
        .orElseThrow(() -> new NotFoundException("Aluno %s nao encontrado".formatted(cmd.getAlunoId())));
    var aluno = AlunoMapper.toDomain(alunoPer);

    if(!aluno.canBeDeleted()) {
      throw new DomainException("Aluno %s nao pode ser excluído".formatted(aluno.getAlunoId()));
    }
    
    alunoPer.getTurmas().forEach(t -> {
      t.getAlunos().remove(alunoPer);
      turmaRepository.save(t); // remove o aluno da turma no banco de dados
      TurmaMapper.toDomain(t).desmatricularAluno(mediator, aluno); // emite o evento de domínio
    });

    alunoRepository.delete(alunoPer); // exclui o aluno no banco de dados
    aluno.excluirAluno(mediator); // emite o evento de domínio
    
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
