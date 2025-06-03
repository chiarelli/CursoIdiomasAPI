package com.github.chiarelli.curso_idiomas_api.escola.presentation.rest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.curso_idiomas_api.escola.application.queries.PageListarAlunosQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarAlunoPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.AlunoJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.AlunoJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.CriarAlunoJsonRequest;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos.PageCollectionJsonResponse;

import io.jkratz.mediator.core.Mediator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/alunos")
public class AlunosController {

  @Autowired Mediator mediator;

  @PostMapping
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
  public AlunoJsonResponse atualizarAluno(
    @PathVariable UUID id, 
    @RequestBody AlunoJsonRequest request
  ) {
    // TODO implementar atualizacao de aluno
    throw new UnsupportedOperationException("implement method atualizarAluno");
  }
    
  @DeleteMapping("{id}")
  public void excluirAluno(@PathVariable UUID id) {
    // TODO implementar busca de aluno por id
    throw new UnsupportedOperationException("implement method buscarAlunoPorId");
  }

  @GetMapping("{alunoId}/turma/{turmaId}")
  public List<AlunoJsonResponse> listarAlunosDaTurma(
    @PathVariable UUID alunoId,
    @PathVariable UUID turmaId
    ) {
    // TODO implementar listagem de alunos da turma
    throw new UnsupportedOperationException("implement method listarAlunosDaTurma");
  }  
}
