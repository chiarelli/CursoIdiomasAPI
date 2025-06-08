package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.AtualizarDadosAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoAtualizadoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.AlunoActions;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;

@Component
public class AlterarDadosAlunoUseCase implements RequestHandler<AtualizarDadosAlunoCommand, AlunoInterface> {

  private final AlunoActions alunoActions;
  private final AlunoRepository repository;

  public AlterarDadosAlunoUseCase(
    AlunoRepository repository,
    Mediator mediator,
    InstanceValidator validator
    ) {
      this.repository = repository;

      this.alunoActions = new AlunoActions(mediator, validator);
  }

  @Override
  @Transactional
  public AlunoInterface handle(AtualizarDadosAlunoCommand cmd) {
    var alunoP = repository.findById(cmd.getAlunoId())
      .orElseThrow(() -> new ResourceNotFoundException("Aluno %s nao encontrado".formatted(cmd.getAlunoId())));

    alunoP.setNome(cmd.getNome());
    alunoP.setEmail(cmd.getEmail());

    var aluno = AlunoMapper.toDomain(alunoP);

    alunoP = repository.save(alunoP);

    alunoActions.atualizarDadosAluno(aluno); // Emite o evento

    return AlunoMapper.toDomain(alunoP);
  }

  @Component
  public static class AlunoAtualizadoHandler implements EventHandler<AlunoAtualizadoEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AlterarDadosAlunoUseCase.class);
    @Override
    public void handle(AlunoAtualizadoEvent event) {
      logger.info("Aluno {} atualizado", event.getAluno().getAlunoId());
    }
    
  }

}
