package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoCadastradoEvent;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoExcluidoEvent;

import io.jkratz.mediator.core.Mediator;

public class AlunoActions extends AbstractAggregateActions {

  public AlunoActions(Mediator mediator, InstanceValidator validator) {
    super(mediator, validator);
  }

  public void cadastrarAluno(Aluno aluno) {
    validator.validate(aluno);
    mediator.emit(new AlunoCadastradoEvent(aluno.getAlunoId()));
  }

  public void atualizarDadosAluno(Aluno aluno) {
    validator.validate(aluno);
    throw new IllegalArgumentException("implement method atualizarDadosAluno");
  }
  
  public void excluirAluno(Aluno aluno) {
    if(!aluno.canBeDeleted())
      throw new DomainException("Aluno %s nao pode ser excluído, pois ainda esta matriculado em pelo menos uma turma".formatted(aluno.getAlunoId()));
    
    validator.validate(aluno); // Valida o aluno após as regras de negócio
    
    aluno.clear();
    mediator.emit(new AlunoExcluidoEvent(aluno.getAlunoId()));
  }

}
