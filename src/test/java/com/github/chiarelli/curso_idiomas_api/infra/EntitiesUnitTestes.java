package com.github.chiarelli.curso_idiomas_api.infra;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarAlunoUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarTurmaUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.MatricularAlunoEmTurmaUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.MatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Turma;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.NotFoundException;

@SpringBootTest
public class EntitiesUnitTestes {

  @Autowired AlunoRepository alunoRepository;
  @Autowired TurmaRepository turmaRepository;

  @Test
  void alunoEntitySalvarComDadosInvalidos() {
    // Prepare
    alunoRepository.deleteAll();

    // Aluno OK
    var alunoId = UUID.randomUUID();
    var alunoOK = new AlunoPersistence(alunoId, "Aluno 1", "166.723.870-13", "8f6wZ@example.com");
    assertDoesNotThrow(() -> alunoRepository.save(alunoOK));

    // Tentar salvar aluno mesmo CPF
    var alunoInvalid = new AlunoPersistence(UUID.randomUUID(), "Aluno 2", "166.723.870-13", "782tE@example.com");
    var errorMessage = assertThrows(DataIntegrityViolationException.class, () -> alunoRepository.save(alunoInvalid)).getMessage();
    
    assertTrue(errorMessage.contains("The duplicate key value is (16672387013)"));

    // Aluno com email duplicado
    var alunoInvalid2 = new AlunoPersistence(UUID.randomUUID(), "Aluno 3", "400.626.040-74", "8f6wZ@example.com");
    var errorMessage2 = assertThrows(DataIntegrityViolationException.class, () -> alunoRepository.save(alunoInvalid2)).getMessage();
    
    assertTrue(errorMessage2.contains("The duplicate key value is (8f6wZ@example.com)"));
    
    // Aluno com email invalido
    var alunoInvalid3 = new AlunoPersistence(UUID.randomUUID(), "Aluno 4", "897.001.890-58", "invalid_email");
    assertThrows(TransactionSystemException.class, () -> alunoRepository.save(alunoInvalid3)).getMessage();
    
  }

  @Autowired CadastrarAlunoUseCase cadastrarAluno;
  @Autowired CadastrarTurmaUseCase cadastrarTurma;

  @Test
  void cadastrarNovoAlunoSemTurma() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    AtomicReference<TurmaInterface> turma = new AtomicReference<>();
    AtomicReference<AlunoInterface> aluno = new AtomicReference<>();

    assertDoesNotThrow(() -> {
      var t = cadastrarTurma.handle(
        new CadastrarNovaTurmaCommand(
          125,
          2025
      ));
      turma.set(t);
    });
    // System.out.println("Turma: " + turma.get().toString());

    assertDoesNotThrow(() -> {
      var a = cadastrarAluno.handle(
        new RegistrarNovoAlunoCommand(
          "Aluno 1",
          "998.180.620-00",
          "8f6wZ@example.com",
          Set.of(turma.get().getTurmaId())
        )
      );
      aluno.set(a);
    });
    // System.out.println("Aluno: " + aluno.get().toString());

    // Tentar cadastrar um aluno sem turma
    var errorMsgs1 = assertThrows(DomainException.class, () -> {
      cadastrarAluno.handle(
        new RegistrarNovoAlunoCommand(
          "Aluno 2",
          "360.756.310-11",
          "782tE@example.com",
          Set.of()
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs1.get("error").equals("Aluno deve ser matriculado em pelo menos uma turma"), 
      "deveria apresentar mensagem \"Aluno deve ser matriculado em pelo menos uma turma\""
    );

    // Tentar cadastrar um aluno em turma inexistente
    var errorMsgs2 = assertThrows(DomainException.class, () -> {
      cadastrarAluno.handle(
        new RegistrarNovoAlunoCommand(
          "Aluno 3",
          "360.756.310-11",
          "782tE@example.com",
          Set.of(UUID.randomUUID())
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs2.get("error").equals("Aluno deve ser matriculado em pelo menos uma turma"), 
      "deveria apresentar mensagem \"Aluno deve ser matriculado em pelo menos uma turma\""
    );

  }
  
  @Test
  void cadastrarAlunoAcimaLotacaoTurma() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    assertTrue(5 == Turma.LOTACAO_MAX, "este teste foi configurado para lotação máxima de 5 alunos na turma, porém a lotação atual é " + Turma.LOTACAO_MAX);

    // Cadastrar turma
    var turma =cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // Cadastrar 5 alunos na turma
    cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "540.219.940-09",
        "782tE@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 2",
        "521.541.110-76",
        "342tE@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 3",
        "915.302.090-13",
        "483tE@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 4",
        "695.231.070-38",
        "896tE@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 5",
        "596.037.800-06",
        "8f6wZ@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    // Tentar cadastrar o sexto aluno deve dar execação
    var errorMsgs = assertThrows(DomainException.class, () -> {
      cadastrarAluno.handle(
        new RegistrarNovoAlunoCommand(
          "Aluno 6",
          "695.231.070-38",
          "099ti@example.com",
          Set.of(turma.getTurmaId())
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs.get("alunos").equals("Lotação da turma excedida"), 
      "deveria apresentar mensagem \"Lotação da turma excedida\""
    );

  }

  @Test
  void cadastrarNovoAlunoDadosInvalidos() {
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma
    var turma =cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // CPF invalido
    var errorMsgs1 = assertThrows(DomainException.class, () -> {
      cadastrarAluno.handle(
        new RegistrarNovoAlunoCommand(
          "Aluno 1",
          "123.456.789-10",
          "782tE@example.com",
          Set.of(turma.getTurmaId())
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs1.get("cpf").equals("número do registro de contribuinte individual brasileiro (CPF) inválido"), 
      "deveria apresentar mensagem \"número do registro de contribuinte individual brasileiro (CPF) inválido\""
    );

    // Email invalido
    var errorMsgs2 = assertThrows(DomainException.class, () -> {
      cadastrarAluno.handle(
        new RegistrarNovoAlunoCommand(
          "Aluno 1",
          "596.037.800-06",
          "invalid_email",
          Set.of(turma.getTurmaId())
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs2.get("email").equals("deve ser um endereço de e-mail bem formado"), 
      "deveria apresentar mensagem \"deve ser um endereço de e-mail bem formado\""
    );
  }

  @Test
  void cadastrarTurmaComDadosInvalidos() {
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma
    var turma =cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // Tentar cadastrar turma com dados inválidos
    var errorMsgs = assertThrows(DomainException.class, () -> {
      cadastrarTurma.handle(
        new CadastrarNovaTurmaCommand(
          0,
          1899
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs.get("anoLetivo").equals("deve ser maior que ou igual à 1900"), 
      "deveria apresentar mensagem \"deve ser maior que ou igual à 1900\""
    );

    assertTrue(
      errorMsgs.get("numeroTurma").equals("deve ser maior que ou igual à 1"), 
      "deveria apresentar mensagem \"deve ser maior que ou igual à 1\""
    );

    // Tentar cadastrar turma com mesmo turmaId, porém anoLetivo diferente
    assertDoesNotThrow(() -> {
      cadastrarTurma.handle(
        new CadastrarNovaTurmaCommand(
          turma.getNumeroTurma(),
          turma.getAnoLetivo() + 1
        )
      );
    });

    // Tentar cadastrar turma com mesmo turmaId no mesmo anoLetivo
    var errorMsgs2 = assertThrows(DomainException.class, () -> {
      cadastrarTurma.handle(
        new CadastrarNovaTurmaCommand(
          turma.getNumeroTurma(),
          turma.getAnoLetivo()
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsgs2.get("error").equals("Turma com id "+ turma.getNumeroTurma() +" já cadastrada"), 
      "deveria apresentar mensagem \"Turma com id "+ turma.getNumeroTurma() +" já cadastrada\""
    );

    
  }

  @Autowired MatricularAlunoEmTurmaUseCase matricularAluno;
  
  @Test
  void matricularAlunoEmTurma() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar primeira turma
    var turma = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // Cadastrar aluno
    var aluno =cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "736.260.510-21",
        "gG0wI@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    // Cadastrar segunda turma
    var turma2 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        500,
        2026
    ));

    // Matricular aluno
    matricularAluno.handle(
      new MatricularAlunoTurmaCommand(
        turma2.getTurmaId(), 
        aluno.getAlunoId()
      )
    );

    // Matricular aluno em turma já matriculado
    var errorMsg1 = assertThrows(DomainException.class, () -> {
      matricularAluno.handle(
        new MatricularAlunoTurmaCommand(
          turma.getTurmaId(),
          aluno.getAlunoId()
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsg1.get("error").equals("Aluno %s ja matriculado na turma %s".formatted(aluno.getAlunoId(), turma.getTurmaId())), 
      "deveria apresentar mensagem \"Aluno %s ja matriculado na turma %s\""
    );

    // Matricular aluno turma inexistente
    var turmaIdInvalido = UUID.randomUUID();

    var errorMsg = assertThrows(NotFoundException.class, () -> {
      matricularAluno.handle(
        new MatricularAlunoTurmaCommand(
          turmaIdInvalido,
          aluno.getAlunoId()
        )
      );
    }).getMessage();

    assertTrue(
      errorMsg.equals("Turma %s nao encontrada".formatted(turmaIdInvalido)), 
      "deveria apresentar mensagem \"Turma %s nao encontrada\""
    );

    // Matricular aluno aluno inexistente
    var alunoIdInvalido = UUID.randomUUID();

    var errorMsg2 = assertThrows(NotFoundException.class, () -> {
      matricularAluno.handle(
        new MatricularAlunoTurmaCommand(
          turma2.getTurmaId(),
          alunoIdInvalido
        )
      );
    }).getMessage();

    assertTrue(
      errorMsg2.equals("Aluno %s nao encontrado".formatted(alunoIdInvalido)), 
      "deveria apresentar mensagem \"Aluno %s nao encontrado\""
    );

  }

}
