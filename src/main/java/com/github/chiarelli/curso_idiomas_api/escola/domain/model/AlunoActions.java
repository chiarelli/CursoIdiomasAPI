package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.events.AlunoAtualizadoEvent;
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
    
    mediator.emit(new AlunoAtualizadoEvent(aluno));
  }
  
  public void excluirAluno(Aluno aluno) {    
    if(aluno.getTurmas().size() > 0) {
      throw new DomainException("Aluno %s nao pode ser excluído, pois ainda esta matriculado em pelo menos uma turma".formatted(aluno.getAlunoId()));
    }
    
    mediator.emit(new AlunoExcluidoEvent(aluno.getAlunoId()));
    aluno.clear();
  }

}
