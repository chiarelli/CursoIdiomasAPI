package com.github.chiarelli.curso_idiomas_api.escola.presentation.rest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.ListarTurmasDoAlunoQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.PageListarTurmasQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarTurmaPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.AtualizarDadosTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.NovaTurmaJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.PageCollectionJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.TurmaJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.TurmaJsonResponse;

import io.jkratz.mediator.core.Mediator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/turmas")
@Tag(name = "Turmas", description = "Endpoints para gerenciamento de turmas")
public class TurmasController {

  private final Mediator mediator;

  public TurmasController(Mediator mediator) {
    this.mediator = mediator;
  }

  @PostMapping
  @Operation(
    summary = "Cadastrar turma",
    description = "Cria uma nova turma.",
    responses = {
      @ApiResponse(responseCode = "201", description = "Turma criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro ao criar turma")
    }
  )
  public TurmaJsonResponse cadastrarTurma(@RequestBody NovaTurmaJsonRequest request) {
    var cmd = new CadastrarNovaTurmaCommand(request.getNumeroTurma(), request.getAnoLetivo());
    var result = mediator.dispatch(cmd);
    return new TurmaJsonResponse(result.getTurmaId(), result.getNumeroTurma(), result.getAnoLetivo(), null);
  }

  @GetMapping
  @Operation(
    summary = "Listar turmas",
    description = "Lista todas as turmas com paginação."
  )
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
  @Operation(
    summary = "Buscar turma por id",
    description = "Retorna uma turma pelo id."
  )
  public TurmaJsonResponse buscarTurmaPorId(@PathVariable UUID id) {
    var query = new RecuperarTurmaPeloIdQuery(id);
    var result = mediator.dispatch(query);
    return new TurmaJsonResponse(
      result.getTurmaId(),
      result.getNumeroTurma(), 
      result.getAnoLetivo(), 
      result.getAlunos().stream()
        .map(aluno -> aluno.getAlunoId())
        .collect(Collectors.toSet())
    );
  }

  @PutMapping("{id}")
  @Operation(
    summary = "Atualizar turma",
    description = "Atualiza os dados de uma turma",
    responses = {
      @ApiResponse(responseCode = "200", description = "Turma atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro ao atualizar turma"),
      @ApiResponse(responseCode = "404", description = "Turma não encontrada")
    }
  )
  public TurmaJsonResponse atualizarTurma(
    @PathVariable UUID id, 
    @RequestBody TurmaJsonRequest turmaJsonRequest
  ) {
    var cmd = new AtualizarDadosTurmaCommand(
      id,
      turmaJsonRequest.getNumeroTurma(),
      turmaJsonRequest.getAnoLetivo()
    );

    var result = mediator.dispatch(cmd);
    return new TurmaJsonResponse(
      result.getTurmaId(),
      result.getNumeroTurma(), 
      result.getAnoLetivo(), 
      result.getAlunos().stream()
        .map(aluno -> aluno.getAlunoId())
        .collect(Collectors.toSet())
    );
  }

  @DeleteMapping("{id}")
  @ApiResponse
  @Operation(
    summary = "Excluir turma",
    description = "Exclui uma turma definitivamente",
    responses = {
      @ApiResponse(responseCode = "204", description = "Turma excluída com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro ao excluir turma"),
    }
  )
  public ResponseEntity<Void> excluirTurma(@PathVariable UUID id) {
    var cmd = new ExcluirTurmaCommand(id);
    mediator.dispatch(cmd);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("aluno/{alunoId}")
  @Operation(
    summary = "Listar turmas do aluno",
    description = "Listar todas as turmas do um aluno"
  )
  public List<TurmaJsonResponse> listarTurmasDoAluno(
    @PathVariable UUID alunoId
  ) {
    
    var turmas = mediator.dispatch(new ListarTurmasDoAlunoQuery(alunoId));

    return turmas.stream()
      .map(turma -> new TurmaJsonResponse(
        turma.getTurmaId(),
        turma.getNumeroTurma(), 
        turma.getAnoLetivo(), 
        turma.getAlunos().stream()
          .map(aluno -> aluno.getAlunoId())
          .collect(Collectors.toSet())
      )
    ).collect(Collectors.toList());  
  }

}
