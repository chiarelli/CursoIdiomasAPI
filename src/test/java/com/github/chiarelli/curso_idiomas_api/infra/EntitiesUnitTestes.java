package com.github.chiarelli.curso_idiomas_api.infra;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoPersistence;
import com.github.chiarelli.curso_idiomas_api.escola.infra.persistence.AlunoRepository;

@SpringBootTest
public class EntitiesUnitTestes {

  @Autowired AlunoRepository alunoRepository;

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
}
