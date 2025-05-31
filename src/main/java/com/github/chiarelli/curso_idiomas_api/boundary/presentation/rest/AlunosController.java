package com.github.chiarelli.curso_idiomas_api.boundary.presentation.rest;

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

import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.AlunoJsonRequest;
import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.AlunoJsonResponse;
import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.CriarAlunoJsonRequest;
import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.PageCollectionJsonResponse;
import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.TurmaJsonResponse;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarAlunoPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;

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
    @RequestParam @Min(1) Integer page,
    @RequestParam @Min(1) @Max(100) Integer size
  ) {
    // TODO implementar listagem de alunos
    throw new UnsupportedOperationException("implement method listarAlunos");
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

  @GetMapping("{id}/turmas")
  public List<TurmaJsonResponse> listarTurmasDoAluno(@PathVariable UUID id) {
    // TODO implementar listagem de turmas do aluno
    throw new UnsupportedOperationException("implement method listarTurmasDoAluno");
  }

}
