package com.github.chiarelli.curso_idiomas_api.escola.application.usecases;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaMapper;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaRepository;

import io.jkratz.mediator.core.RequestHandler;
import jakarta.transaction.Transactional;

@Component
public class CadastrarAlunoUseCase implements RequestHandler<RegistrarNovoAlunoCommand, AlunoInterface> {

  @Autowired InstanceValidator validator;
  @Autowired AlunoRepository alRepo;
  @Autowired TurmaRepository trRepo;

  @Override
  @Transactional
  public AlunoInterface handle(RegistrarNovoAlunoCommand cmd) {

    var turmasPers = trRepo.findAllByTurmaId(cmd.getTurmaMatricularIds());

    if(turmasPers.size() < 1) {
      throw new DomainException("O aluno deve ser matriculado em pelo menos uma turma");
    }

    // Mapeia o payload do comando para o aggregate
    var aluno = RegistrarNovoAlunoCommand.toDomain(UUID.randomUUID(), cmd);

    // Valida se aluno pode ser adicionado às turmas
    var turmas = turmasPers.stream()
      .map(TurmaMapper::toDomain)
      .collect(Collectors.toSet());

    turmas.forEach(t -> {
      t.adicionarAluno(aluno);
      aluno.addTurma(t);
      validator.validate(t);
    });

    // Valida o aluno domain
    validator.validate(aluno);

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

    return AlunoMapper.toDomain(alunoPers);
  }

}
