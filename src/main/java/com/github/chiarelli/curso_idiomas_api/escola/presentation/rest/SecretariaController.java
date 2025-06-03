package com.github.chiarelli.curso_idiomas_api.escola.presentation.rest;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/secretaria")
public class SecretariaController {

  @PostMapping("matricular/turma/{turmaId}/aluno/{alunoId}")
  @Operation(
    summary = "Matricular aluno na turma",
    responses = {
      @ApiResponse(responseCode = "204", description = "Aluno matriculado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Erro ao matricular aluno"),
      @ApiResponse(responseCode = "404", description = "Aluno ou turma não encontrados"),
    }
    )
    public ResponseEntity<Void> matricularAluno(
      @PathVariable UUID turmaId, 
      @PathVariable UUID alunoId
      ) {
        // TODO implementar matricula de aluno na turma
        throw new UnsupportedOperationException("implement method matricularAluno");
      }
      
      @DeleteMapping("desmatricular/turma/{turmaId}/aluno/{alunoId}")
      @Operation(
        summary = "Desmatricular aluno na turma",
        responses = {
          @ApiResponse(responseCode = "204", description = "Aluno desmatriculado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Erro ao desmatricular aluno"),
          @ApiResponse(responseCode = "404", description = "Aluno ou turma não encontrados"),
    }
  )
  public ResponseEntity<Void> desmatricularAlunoTurma(
    @PathVariable UUID turmaId, 
    @PathVariable UUID alunoId
  ) {
    // TODO implementar matricula de aluno na turma
    throw new UnsupportedOperationException("implement method matricularAluno");
  }

}
