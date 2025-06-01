package com.github.chiarelli.curso_idiomas_api.escola.presentation.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.AlunoJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.NovaTurmaJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.PageCollectionJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.TurmaJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.TurmaJsonResponse;

import io.jkratz.mediator.core.Mediator;

@RestController
@RequestMapping("api/v1/turmas")
public class TurmasController {

  @Autowired Mediator mediator;

  @PostMapping
  public TurmaJsonResponse cadastrarTurma(@RequestBody NovaTurmaJsonRequest request) {
    var cmd = new CadastrarNovaTurmaCommand(request.getTurmaId(), request.getAnoLetivo());
    var result = mediator.dispatch(cmd);
    return new TurmaJsonResponse(result.getTurmaId(), result.getAnoLetivo(), null);
  }

  @GetMapping
  public PageCollectionJsonResponse<TurmaJsonResponse> listarTurmas() {
    // TODO implementar listagem de turmas
    throw new UnsupportedOperationException("implement method listarTurmas");
  }

  @GetMapping("{id}")
  public TurmaJsonResponse buscarTurmaPorId(@PathVariable UUID id) {
    // TODO implementar busca de turma por id
    throw new UnsupportedOperationException("implement method buscarTurmaPorId");
  }

  @PutMapping("{id}")
  public TurmaJsonResponse atualizarTurma(
    @PathVariable UUID id, 
    @RequestBody TurmaJsonRequest turmaJsonRequest
  ) {
    // TODO implementar atualizacao de turma
    throw new UnsupportedOperationException("implement method atualizarTurma");
  }

  @DeleteMapping("{id}")
  public void excluirTurma(@PathVariable UUID id) {
    // TODO implementar exclusao de turma
    throw new UnsupportedOperationException("implement method excluirTurma");
  }

  // GET /api/v1/turmas/{id}/alunos – Listar todos os alunos da turma

  @GetMapping("{id}/alunos")
  public List<AlunoJsonResponse> listarAlunosDaTurma(@PathVariable UUID id) {
    // TODO implementar listagem de alunos da turma
    throw new UnsupportedOperationException("implement method listarAlunosDaTurma");
  }

}
