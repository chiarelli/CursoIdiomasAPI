package com.github.chiarelli.curso_idiomas_api.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.chiarelli.curso_idiomas_api.escola.domain.DomainException;
import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;
import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Aluno;
import com.github.chiarelli.curso_idiomas_api.escola.domain.model.Turma;

@SpringBootTest
public class AggregatesUnitTests {

  @Autowired InstanceValidator validator;

  @Test
  void alunoAggregateValidation() {
    var alunoOk = new Aluno(UUID.randomUUID(), "Teste", "390.898.981-74", "8f6wZ@example.com", Set.of());

    assertDoesNotThrow(() -> validator.validate(alunoOk));

    var alunoInvalid = new Aluno(null, "Te", "123.456.789-10", "invalid_email", Set.of());

    var userMessages = assertThrows(DomainException.class, () -> validator.validate(alunoInvalid))
      .getUserMessages();

    assertTrue(
      userMessages.get("cpf").equals("número do registro de contribuinte individual brasileiro (CPF) inválido"), 
      "deveria apresentar mensagem \"número do registro de contribuinte individual brasileiro (CPF) inválido\""
    );

    assertTrue(
      userMessages.get("email").equals("deve ser um endereço de e-mail bem formado"), 
      "deveria apresentar mensagem \"deve ser um endereço de e-mail bem formado"
    );

    assertTrue(
      userMessages.get("nome").equals("tamanho deve ser entre 3 e 100"), 
      "deveria apresentar mensagem \"tamanho deve ser entre 3 e 100\""
    );

    assertTrue(
      userMessages.get("alunoId").equals("não deve ser nulo"), 
      "deveria apresentar mensagem \"não deve ser nulo\""
    );

  }

  @Test
  void turmaAggregateValidation() {
    TurmaInterface turmaOk = new Turma(302, 2025);

    assertDoesNotThrow(() -> validator.validate(turmaOk));

    TurmaInterface turmaInvalid1 = new Turma(0, 1899);

    var userMessages = assertThrows(DomainException.class, () -> validator.validate(turmaInvalid1))
      .getUserMessages();

    assertTrue(
      userMessages.get("anoLetivo").equals("deve ser maior que ou igual à 1900"), 
      "deveria apresentar mensagem \"deve ser maior que ou igual à 1900\""
    );

    assertTrue(
      userMessages.get("turmaId").equals("deve ser maior que ou igual à 1"), 
      "deveria apresentar mensagem \"deve ser maior que ou igual à 1\""
    );

    TurmaInterface turmaInvalid2 = new Turma(-1, 2500);

    userMessages = assertThrows(DomainException.class, () -> validator.validate(turmaInvalid2))
      .getUserMessages();

    // {anoLetivo=deve ser menor que ou igual à 2399, turmaId=deve ser maior que ou igual à 1}

    assertTrue(
      userMessages.get("anoLetivo").equals("deve ser menor que ou igual à 2399"), 
      "deveria apresentar mensagem \"deve ser menor que ou igual à 2399\""
    );

    assertTrue(
      userMessages.get("turmaId").equals("deve ser maior que ou igual à 1"), 
      "deveria apresentar mensagem \"deve ser maior que ou igual à 1\""
    );

    TurmaInterface turmaInvalid3 = new Turma(null, 2025);

    userMessages = assertThrows(DomainException.class, () -> validator.validate(turmaInvalid3))
      .getUserMessages();

    assertTrue(
      userMessages.get("turmaId").equals("não deve ser nulo"), 
      "deveria apresentar mensagem \"não deve ser nulo\""
    );
  }
  
}
