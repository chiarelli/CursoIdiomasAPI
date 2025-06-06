package com.github.chiarelli.curso_idiomas_api.escola.presentation.rest;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.DesmatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.MatricularAlunoTurmaCommand;

import io.jkratz.mediator.core.Mediator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/secretaria")
@Tag(name = "Secretaria", description = "Endpoints para tarefas administrativas da escola")
public class SecretariaController {

  private final Mediator mediator;

  public SecretariaController(Mediator mediator) {
    this.mediator = mediator;
  }

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
        var cmd = new MatricularAlunoTurmaCommand(turmaId, alunoId);
        mediator.dispatch(cmd);

        return ResponseEntity.noContent().build();
      }
      
      @DeleteMapping("desmatricular/turma/{turmaId}/aluno/{alunoId}")
      @Operation(
        summary = "Desmatricular aluno da turma",
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
    var cmd = new DesmatricularAlunoTurmaCommand(turmaId, alunoId);
    mediator.dispatch(cmd);

    return ResponseEntity.noContent().build();
  }

}
