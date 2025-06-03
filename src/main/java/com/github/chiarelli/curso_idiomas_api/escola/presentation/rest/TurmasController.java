package com.github.chiarelli.curso_idiomas_api.escola.presentation.rest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.PageListarTurmasQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.NovaTurmaJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.PageCollectionJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.TurmaJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.TurmaJsonResponse;

import io.jkratz.mediator.core.Mediator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/turmas")
public class TurmasController {

  private final Mediator mediator;

  public TurmasController(Mediator mediator) {
    this.mediator = mediator;
  }

  @PostMapping
  public TurmaJsonResponse cadastrarTurma(@RequestBody NovaTurmaJsonRequest request) {
    var cmd = new CadastrarNovaTurmaCommand(request.getNumero(), request.getAnoLetivo());
    var result = mediator.dispatch(cmd);
    return new TurmaJsonResponse(result.getTurmaId(), result.getNumeroTurma(), result.getAnoLetivo(), null);
  }

  @GetMapping
  public PageCollectionJsonResponse<TurmaJsonResponse> listarTurmas(
    @RequestParam(name = "page", defaultValue = "1") @Min(1) Integer page,
    @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
  ) {
    var query = new PageListarTurmasQuery(page, size);
    var result = mediator.dispatch(query);

    return new PageCollectionJsonResponse<>(
      result.getNumberOfElements(),
      page,
      result.getSize(),
      result.getTotalElements(),
      result.getTotalPages(),
      result.getContent().stream()
          .map(turma -> new TurmaJsonResponse(
              turma.getTurmaId(),
              turma.getNumeroTurma(), 
              turma.getAnoLetivo(), 
              turma.getAlunos().stream()
                  .map(aluno -> aluno.getAlunoId())
                  .collect(Collectors.toSet())
          )
      ).collect(Collectors.toList())
    );
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

  @GetMapping("{turmaId}/aluno/{alunoId}")
  public List<TurmaJsonResponse> listarTurmasDoAluno(
    @PathVariable UUID turmaId, 
    @PathVariable UUID alunoId
  ) {
    // TODO implementar listagem de turmas do aluno
    throw new UnsupportedOperationException("implement method listarTurmasDoAluno");
  }

}
