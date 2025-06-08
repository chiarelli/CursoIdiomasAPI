package com.github.chiarelli.curso_idiomas_api.infra;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import com.github.chiarelli.curso_idiomas_api.AbstractIntegrationTest;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarAlunoPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.queries.RecuperarTurmaPeloIdQuery;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.AlterarDadosAlunoUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.AtualizarDadosTurmaUsecase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarAlunoUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.CadastrarTurmaUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.DesmatricularAlunoEmTurmaUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.ExcluirAlunoUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.ExcluirTurmaUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.MatricularAlunoEmTurmaUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.RecuperarAlunoPeloIdUseCase;
import com.github.chiarelli.curso_idiomas_api.escola.application.usecases.RecuperarTurmaPeloIdUsecase;
import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.AtualizarDadosAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.AtualizarDadosTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.CadastrarNovaTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.DesmatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.ExcluirTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.MatricularAlunoTurmaCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.commands.RegistrarNovoAlunoCommand;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Turma;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.AlunoRepository;
import com.github.chiarelli.curso_idiomas_api.escola.infra.jpa.TurmaRepository;
import com.github.chiarelli.curso_idiomas_api.escola.presentation.exceptions.ResourceNotFoundException;

public class EntitiesUnitTests extends AbstractIntegrationTest {

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
          "467.739.240-49",
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
  void cadastrarNovoAlunoVerificarAtributos() {
    // prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma
    var turma =cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        496,
        2025
    ));

    // Cadastrar aluno
    var aluno = cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "227.946.130-73",
        "782tE@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    // Verificar atributos
    assertDoesNotThrow(() -> UUID.fromString(aluno.getAlunoId().toString()));
    assertEquals("Aluno 1", aluno.getNome());
    assertEquals("22794613073", aluno.getCpf());
    assertEquals("782tE@example.com", aluno.getEmail());
    assertTrue(aluno.getTurmas().contains(turma));

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
      errorMsgs2.get("error").equals("Turma com numero %s e ano letivo %s já cadastrada".formatted(turma.getNumeroTurma(), turma.getAnoLetivo())),
      "deveria apresentar mensagem \"Turma com numero %s e ano letivo %s já cadastrada\"".formatted(turma.getNumeroTurma(), turma.getAnoLetivo())
    );
    
  }

  @Test
  void cadastrarNovoTurmaVerificarAtributos() {
    // prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma
    var turma =cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        496,
        2025
    ));

    // Verificar atributos
    assertDoesNotThrow(() -> UUID.fromString(turma.getTurmaId().toString()));
    assertEquals(496, turma.getNumeroTurma());
    assertEquals(2025, turma.getAnoLetivo());
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

    var errorMsg = assertThrows(ResourceNotFoundException.class, () -> {
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

    var errorMsg2 = assertThrows(ResourceNotFoundException.class, () -> {
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

  @Autowired DesmatricularAlunoEmTurmaUseCase desmatricularAluno;
  @Autowired RecuperarAlunoPeloIdUseCase recuperarAluno;
  
  @Test
  void desmatricularAlunoEmTurma() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma1
    var turma1 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // Cadastrar turma2
    var turma2 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        630,
        2025
    ));

    // Cadastrar aluno
    var aluno = cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "487.492.760-26",
        "gG0wI@example.com",
        Set.of(turma1.getTurmaId(), turma2.getTurmaId())
      )
    );

    // Verificar se aluno foi matriculado nas duas turmas
    var alunoRecuperado = recuperarAluno.handle(
      new RecuperarAlunoPeloIdQuery(
        aluno.getAlunoId()
      )
    );

    assertEquals(2, alunoRecuperado.getTurmas().size());
    assertTrue(alunoRecuperado.getTurmas().contains(turma1));
    assertTrue(alunoRecuperado.getTurmas().contains(turma2));

    // Desmatricular aluno da turma1
    desmatricularAluno.handle(
      new DesmatricularAlunoTurmaCommand(
        turma1.getTurmaId(),
        alunoRecuperado.getAlunoId()
      )
    );

    // Verificar se aluno foi desmatriculado da turma1
    alunoRecuperado = recuperarAluno.handle(
      new RecuperarAlunoPeloIdQuery(
        alunoRecuperado.getAlunoId()
      )
    );

    assertEquals(1, alunoRecuperado.getTurmas().size());
    assertFalse(alunoRecuperado.getTurmas().contains(turma1));
    assertTrue(alunoRecuperado.getTurmas().contains(turma2));

  }
  
  @Autowired ExcluirTurmaUseCase excluirTurma;

  @Test
  void excluirTurma() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma
    var turma1 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // Cadastrar aluno
    cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "355.880.820-79",
        "gG0wI@example.com",
        Set.of(turma1.getTurmaId())
      )
    );

    // Excluir turma com alunos matriculados
    var errorMsg = assertThrows(DomainException.class, () -> {
      excluirTurma.handle(
        new ExcluirTurmaCommand(
          turma1.getTurmaId()
        )
      );
    }).getUserMessages();

    System.out.println(errorMsg);
    assertTrue(
      errorMsg.get("error").equals("Turma "+ turma1.getTurmaId() +" possui alunos matriculados"), 
      "deveria apresentar mensagem \"Turma "+ turma1.getTurmaId() +" possui alunos matriculados\""
    );

    // Excluir turma sem alunos matriculados
    var turma2 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        630,
        2025
    ));

    assertDoesNotThrow(() -> {
      excluirTurma.handle(
        new ExcluirTurmaCommand(
          turma2.getTurmaId()
        )
      );
    });
    
  }

  @Autowired ExcluirAlunoUseCase excluirAluno;
  @Autowired RecuperarTurmaPeloIdUsecase recuperarTurma;

  @Test
  void excluirAluno() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma1
    var turma1 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        985,
        2025
    ));

    // Cadastrar turma1
    var turma2 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        600,
        2025
    ));

    // Cadastrar aluno
    var aluno = cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "444.949.220-05",
        "e4bVbs@example.com",
        Set.of(turma1.getTurmaId(), turma2.getTurmaId())
      )
    );

    // Cadastrar aluno controle
    var aluno2 = cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 2",
        "151.745.140-04",
        "Hs8xv@example.com",
        Set.of(turma1.getTurmaId(), turma2.getTurmaId())
      )
    );

    // Tentar excluir aluno matriculado em duas turmas
    var errorMsg = assertThrows(DomainException.class, () -> {
      excluirAluno.handle(
        new ExcluirAlunoCommand(
          aluno.getAlunoId()
        )
      );
    }).getUserMessages();

    assertTrue(
      errorMsg.get("error").equals("Aluno "+ aluno.getAlunoId() +" nao pode ser excluído"), 
      "deveria apresentar mensagem \"Aluno "+ aluno.getAlunoId() +" nao pode ser excluído\""
    );

    // Desmatriculando aluno da turma1
    desmatricularAluno.handle(
      new DesmatricularAlunoTurmaCommand(
        turma1.getTurmaId(),
        aluno.getAlunoId()
      )
    );

    // Tentar excluir aluno matriculado em duas turmas
    assertDoesNotThrow(() -> {
      excluirAluno.handle(
        new ExcluirAlunoCommand(
          aluno.getAlunoId()
        )
      );
    });

    // Recuperar aluno por ID
    var errorMsg2 = assertThrows(ResourceNotFoundException.class, () -> {
      
      recuperarAluno.handle(
        new RecuperarAlunoPeloIdQuery(
          aluno.getAlunoId()
        )
      );

    }).getMessage();

    System.out.println(errorMsg2);
    assertTrue(
      errorMsg2.contains("aluno id " + aluno.getAlunoId() + " not exists"),
      "deveria apresentar mensagem \"aluno id " + aluno.getAlunoId() + " not exists\""
    );

    // Recuperar turma por ID e verificar alunos
    var turmaRecuperada1 = recuperarTurma.handle(
      new RecuperarTurmaPeloIdQuery(
        turma1.getTurmaId()
      )
    );

    var turmaRecuperada2 = recuperarTurma.handle(
      new RecuperarTurmaPeloIdQuery(
        turma2.getTurmaId()
      )
    );

    assertFalse(turmaRecuperada1.getAlunos().contains(aluno), "Turma1 não deveria conter o aluno %s".formatted(aluno.getAlunoId()));
    assertFalse(turmaRecuperada2.getAlunos().contains(aluno), "Turma2 não deveria conter o aluno %s".formatted(aluno.getAlunoId()));
    
    assertTrue(turmaRecuperada1.getAlunos().contains(aluno2), "Turma1 deveria conter o aluno %s".formatted(aluno2.getAlunoId()));
    assertTrue(turmaRecuperada2.getAlunos().contains(aluno2), "Turma2 deveria conter o aluno %s".formatted(aluno2.getAlunoId()));
    
  }
  
  @Autowired AtualizarDadosTurmaUsecase atualizarTurma;

  @Test
  void atualizarTurma() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();

    // Cadastrar turma1
    var turma1 = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        125,
        2025
    ));

    // Cadastrar turma2
    cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        630,
        2025
    ));

    // Atualizar turma1
    var errorMsg1 = assertThrows(DomainException.class, () -> {

      atualizarTurma.handle(
        new AtualizarDadosTurmaCommand(
          turma1.getTurmaId(),
          630,
          2025
        )
      );

    }).getUserMessages();

    assertTrue(
      errorMsg1.get("error").equals("Turma com numero 630 e ano letivo 2025 já cadastrada"), 
      "deveria apresentar mensagem \"Turma com numero 630 e ano letivo 2025 já cadastrada\""
    );

    // Atualizar turma1

    var turmaAtualizada = atualizarTurma.handle(
      new AtualizarDadosTurmaCommand(
        turma1.getTurmaId(),
        630,
        2026
      )
    );

    assertTrue(
      turmaAtualizada.getTurmaId() == turma1.getTurmaId(), 
      "deveria apresentar turma id " + turma1.getTurmaId()
    );

    assertTrue(
      turmaAtualizada.getAnoLetivo() == 2026, 
      "deveria apresentar ano letivo 2026"
    );

    assertTrue(
      turmaAtualizada.getNumeroTurma() == 630, 
      "deveria apresentar numero turma 630"
    );

    // Atualizar turma1 com dados inválidos
    assertThrows(DomainException.class, () -> {

      atualizarTurma.handle(
        new AtualizarDadosTurmaCommand(
          turma1.getTurmaId(),
          0,
          1899
        )
      );

    }).getUserMessages();
  
  }

  @Autowired AlterarDadosAlunoUseCase atualizarAluno;

  @Test
  void atualizarAluno() {
    // Prepare
    alunoRepository.deleteAll();
    turmaRepository.deleteAll();
    
    // Cadastrar turma
    var turma = cadastrarTurma.handle(
      new CadastrarNovaTurmaCommand(
        450,
        2025
    ));

    // Cadastrar aluno
    var aluno = cadastrarAluno.handle(
      new RegistrarNovoAlunoCommand(
        "Aluno 1",
        "840.467.530-99",
        "gG0wI@example.com",
        Set.of(turma.getTurmaId())
      )
    );

    // Atualizar aluno Ok
    var alunoAtualizado = atualizarAluno.handle(
      new AtualizarDadosAlunoCommand(
        aluno.getAlunoId(),
        "Aluno 1 (atualizado)",
        "yyE7S@example.com"
      )
    );

    assertTrue(
      alunoAtualizado.getAlunoId().equals(aluno.getAlunoId()), 
      "deveria apresentar id " + aluno.getAlunoId()
    );
    
    assertTrue(
      alunoAtualizado.getEmail().equals("yyE7S@example.com"), 
      "deveria apresentar email yyE7S@example.com"
    );

    assertTrue(
      alunoAtualizado.getNome().equals("Aluno 1 (atualizado)"), 
      "deveria apresentar nome Aluno 1 (atualizado)"
    );

    // Atualizar aluno com dados inválidos
    assertThrows(DomainException.class, () -> {

      atualizarAluno.handle(
        new AtualizarDadosAlunoCommand(
          alunoAtualizado.getAlunoId(),
          "ab",
          "yyE7S@"
        )
      );

    });
  
  }


}
