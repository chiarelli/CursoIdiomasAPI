package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoCadastradoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;

import io.jkratz.mediator.core.EventHandler;
import io.jkratz.mediator.core.Mediator;
import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class CadastrarAlunoUseCase implements RequestHandler<RegistrarNovoAlunoCommand, AlunoInterface> {

  private final InstanceValidator validator;
  private final AlunoRepository alRepo;
  private final TurmaRepository trRepo;
  private final Mediator mediator;

  public CadastrarAlunoUseCase(
    InstanceValidator validator,
    AlunoRepository alRepo,
    TurmaRepository trRepo,
    Mediator mediator
  ) {
    this.validator = validator;
    this.alRepo = alRepo;
    this.trRepo = trRepo;
    this.mediator = mediator;
  }

  @Override
  @Transactional
  public AlunoInterface handle(RegistrarNovoAlunoCommand cmd) {

    var turmasPers = trRepo.findAllById(cmd.getTurmaMatricularIds()).stream().collect(Collectors.toSet());
    var turmas = turmasPers.stream().map(TurmaMapper::toDomain).collect(Collectors.toSet());

    var aluno = RegistrarNovoAlunoCommand.toDomain(UUID.randomUUID(), cmd);
    aluno.adicionarTurma(turmas);

    validator.validate(aluno); // Valida o aluno após as regras de negócio
    turmas.forEach(validator::validate); // Valida as turmas após as regras de negócio

    // Mapeia o aluno domain para o persistence
    var alunoPers = new AlunoPersistence(
      aluno.getAlunoId(), 
      aluno.getNome(), 
      aluno.getCpf(), 
      aluno.getEmail(),
      turmasPers
    );

    // Salva o aluno
    alunoPers = alRepo.save(alunoPers);
    
    aluno = AlunoMapper.toDomain(alunoPers);
    aluno.cadastrarAluno(mediator); // Emitir evento de domínio

    return aluno;
  }

  @Component
  public static class AlunoCadastradoHandler implements EventHandler<AlunoCadastradoEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(CadastrarAlunoUseCase.class);

    @Override
    public void handle(AlunoCadastradoEvent event) {
      logger.info("Aluno cadastrado: " + event.getAlunoId());
    }
       
  }

}
