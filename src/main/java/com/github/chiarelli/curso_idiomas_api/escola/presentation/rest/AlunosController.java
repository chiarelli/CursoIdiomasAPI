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

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.ListarAlunosDaTurmaQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.PageListarAlunosQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarAlunoPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.AtualizarDadosAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.AlunoJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.AlunoJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.BadRequestResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.CriarAlunoJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.PageCollectionJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.ResourceNotFoundResponse;

import io.jkratz.mediator.core.Mediator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


@RestController
@RequestMapping("/api/v1/alunos")
@Tag(
  name = "Alunos",
  description = "Endpoints para gerenciamento de alunos"
)
public class AlunosController {

  private final Mediator mediator;

  public AlunosController(Mediator mediator) {
    this.mediator = mediator;
  }

  @PostMapping
  @Operation(
    summary = "Cadastrar aluno", 
    description = "Cria um novo aluno.",
    responses = {
      @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso"),
      @ApiResponse(
        responseCode = "400", 
        description = "Erro ao criar aluno",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = BadRequestResponse.class)
        )
      )
    }
  )
  public ResponseEntity<AlunoJsonResponse> cadastrarAluno(@RequestBody CriarAlunoJsonRequest request) {
    var cmd = new RegistrarNovoAlunoCommand(
      request.getNome(), 
      request.getCpf(), 
      request.getEmail(), 
      request.getTurmaMatricularIds()
    );
    
    var result = mediator.dispatch(cmd);
    var turmaIds = result.getTurmas()
      .stream()
      .map(t -> t.getTurmaId())
      .collect(Collectors.toSet());
    
    var alunoResp = new AlunoJsonResponse(
      result.getAlunoId(), 
      result.getNome(), 
      result.getEmail(), 
      result.getCpf(), 
      turmaIds
    );

    return ResponseEntity.status(201).body(alunoResp);
  }

  @GetMapping
  @Operation(
    summary = "Listar alunos",
    description = "Retorna uma lista de alunos com paginação."
  )
  public PageCollectionJsonResponse<AlunoJsonResponse> listarAlunos(
    @RequestParam(name = "page", defaultValue = "1") @Min(1) Integer page,
    @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
  ) {
    var query = new PageListarAlunosQuery(page, size);
    var result = mediator.dispatch(query);

    return new PageCollectionJsonResponse<>(
        result.getNumberOfElements(),
        page,
        result.getSize(),
        result.getTotalElements(),
        result.getTotalPages(),
        result.getContent().stream().map(aluno ->
            new AlunoJsonResponse(
                aluno.getAlunoId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getCpf(),
                aluno.getTurmas().stream()
                    .map(TurmaInterface::getTurmaId)
                    .collect(Collectors.toSet())
            )
        ).collect(Collectors.toList())
    );
  }

  @GetMapping("{id}")
  @Operation(
    summary = "Buscar aluno por id",
    description = "Retorna um aluno pelo id",
    responses = {
      @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
      @ApiResponse(
        responseCode = "404", 
        description = "Aluno não encontrado",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ResourceNotFoundResponse.class)
        )
      )
    }
  )
  public AlunoJsonResponse buscarAlunoPorId(@PathVariable UUID id) {
    var query = new RecuperarAlunoPeloIdQuery(id);
    var result = mediator.dispatch(query);
    var turmaIds = result.getTurmas()
      .stream()
      .map(t -> t.getTurmaId())
      .collect(Collectors.toSet());

    return new AlunoJsonResponse(
      result.getAlunoId(), 
      result.getNome(), 
      result.getEmail(), 
      result.getCpf(), 
      turmaIds
    );
  }
  
  @PutMapping("{id}")
  @Operation(
    summary = "Atualizar aluno",
    description = "Atualiza os dados de um aluno",
    responses = {
      @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
      @ApiResponse(
        responseCode = "400", 
        description = "Erro ao atualizar aluno",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = BadRequestResponse.class)
        )
      ),
      @ApiResponse(
        responseCode = "404", 
        description = "Aluno não encontrado",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ResourceNotFoundResponse.class)
        )
      )
    }
  )
  public AlunoJsonResponse atualizarAluno(
    @PathVariable UUID id, 
    @RequestBody AlunoJsonRequest request
  ) {
    var cmd = new AtualizarDadosAlunoCommand(
      id, 
      request.getNome(), 
      request.getEmail()
    );
    
    var result = mediator.dispatch(cmd);
    var turmaIds = result.getTurmas()
      .stream()
      .map(t -> t.getTurmaId())
      .collect(Collectors.toSet());
    
    return new AlunoJsonResponse(
      result.getAlunoId(), 
      result.getNome(), 
      result.getEmail(), 
      result.getCpf(), 
      turmaIds
    );
  }
    
  @DeleteMapping("{id}")
  @Operation(
    summary = "Excluir aluno",
    description = "Exclui um aluno definitivamente",
    responses = {
      @ApiResponse(responseCode = "204", description = "Aluno excluído com sucesso"),
      @ApiResponse(
        responseCode = "400", 
        description = "Erro ao excluir aluno",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = BadRequestResponse.class)
        )
      ),
    }
  )
  public ResponseEntity<Void> excluirAluno(@PathVariable UUID id) {
    var cmd = new ExcluirAlunoCommand(id);
    mediator.dispatch(cmd);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("turma/{turmaId}")
  @Operation(
    summary = "Listar alunos da turma",
    description = "Retorna uma lista de todos os alunos da uma turma"
  )
  public List<AlunoJsonResponse> listarAlunosDaTurma(
    @PathVariable UUID turmaId
    ) {
    var query = new ListarAlunosDaTurmaQuery(turmaId);
    var alunos = mediator.dispatch(query);

    return alunos.stream()
      .map(aluno -> new AlunoJsonResponse(
        aluno.getAlunoId(),
        aluno.getNome(),
        aluno.getEmail(),
        aluno.getCpf(),
        aluno.getTurmas().stream()
          .map(TurmaInterface::getTurmaId)
          .collect(Collectors.toSet())
      ))
      .collect(Collectors.toList());
    
  }  
}
