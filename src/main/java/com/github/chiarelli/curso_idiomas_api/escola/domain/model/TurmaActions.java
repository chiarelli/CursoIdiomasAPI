package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoDesmatriculadoTurmaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoMatriculadoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaAtualizadaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaCadastradaEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.TurmaExcluidaEvent;

import io.jkratz.mediator.core.Mediator;

public class TurmaActions extends AbstractAggregateActions {

  public TurmaActions(Mediator mediator, InstanceValidator validator) {
    super(mediator, validator);
  }

  public void criarTurma(Turma turma) {
    validator.validate(turma);
    mediator.emit(new TurmaCadastradaEvent(turma.getTurmaId()));
  }

  public void matricularAluno(Turma turma, Aluno aluno) {
    if(turma.getAlunos().contains(aluno)) {
      throw new DomainException("Aluno %s ja esta matriculado nesta turma".formatted(aluno.getAlunoId()));
    }
    turma.addAluno(aluno);
    aluno.addTurma(turma);

    validator.validate(aluno);
    validator.validate(turma);
    
    mediator.emit(new AlunoMatriculadoEvent(aluno.getAlunoId(), turma.getTurmaId()));
  }

  public void desmatricularAluno(Turma turma, Aluno aluno) {
    if(!turma.getAlunos().contains(aluno)) {
      throw new DomainException("Aluno %s nao esta matriculado nesta turma".formatted(aluno.getAlunoId()));
    }
    turma.removeAluno(aluno);
    aluno.removeTurma(turma);

    validator.validate(aluno);
    validator.validate(turma);

    mediator.emit(new AlunoDesmatriculadoTurmaEvent(aluno.getAlunoId(), turma.getTurmaId()));
  }

  public void atualizarTurma(Turma turma) {
    validator.validate(turma);
    mediator.emit(new TurmaAtualizadaEvent(turma));
  }

  public void excluirTurma(Turma turma) {
    if(!turma.getAlunos().isEmpty()) {
      throw new DomainException("Turma %s possui alunos matriculados".formatted(turma.getTurmaId()));
    }
    validator.validate(turma); // valida a turma

    turma.clear(); // limpa a turma
    mediator.emit(new TurmaExcluidaEvent(turma.getTurmaId()));
  }

}
