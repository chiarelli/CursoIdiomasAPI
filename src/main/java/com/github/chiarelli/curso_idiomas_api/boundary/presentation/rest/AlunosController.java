package com.github.chiarelli.curso_idiomas_api.boundary.presentation.rest;

import java.util.List;
import java.util.UUID;

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
import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.PageCollectionJsonResponse;
import com.github.chiarelli.curso_idiomas_api.boundary.presentation.dtos.TurmaJsonResponse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/alunos")
public class AlunosController {

  @PostMapping
  public ResponseEntity<AlunoJsonResponse> cadastrarAluno(@RequestBody AlunoJsonRequest request) {
    // TODO implementar cadastro de aluno
    throw new UnsupportedOperationException("implement method cadastrarAluno");
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
    // TODO implementar busca de aluno por id
    throw new UnsupportedOperationException("implement method buscarAlunoPorId");
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
