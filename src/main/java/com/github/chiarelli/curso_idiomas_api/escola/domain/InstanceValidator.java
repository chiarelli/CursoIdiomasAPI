package com.github.chiarelli.curso_idiomas_api.escola.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class InstanceValidator {

  @Autowired Validator validator;

  /**
   * Valida a instância recebida contra as constraints definidas em suas anotações.
   * Se violações forem encontradas, lança uma {@link DomainException} com mensagens
   * para o usuário e uma mensagem técnica (opcional, útil para logs) contendo todas as
   * violações. Caso contrário, a instância é válida e nenhuma exceção é lançada.
   *
   * @param instance a instância a ser validada
   * @throws DomainException se violações forem encontradas
   */
  public void validate(Object instance) {
    Set<ConstraintViolation<Object>> violations = validator.validate(instance);

    if (violations.isEmpty()) {
      return; // Nenhuma violação encontrada
    }

    Map<String, Object> userMessages = new LinkedHashMap<>();
    StringBuilder devMessage = new StringBuilder();

    for (ConstraintViolation<Object> violation : violations) {
      String campo = violation.getPropertyPath().toString();
      String msg = violation.getMessage();

      // Preenche map com mensagens para o usuário
      userMessages.put(campo, msg);

      // Mensagem técnica (opcional, útil para logs)
      devMessage.append(campo)
          .append(": ")
          .append(msg)
          .append(" | ");
    }

    var cause = new RuntimeException(devMessage.toString());
    throw new DomainException(userMessages, cause.getMessage(), cause);
  }
}
