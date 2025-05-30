package com.github.chiarelli.curso_idiomas_api.escola.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DomainException extends RuntimeException {
   protected Map<String, Object> userMessages = new HashMap<>(); 

  public DomainException(String userMessage, String message, Throwable cause) {
    super(message, cause);
    userMessages.put("error", userMessage);
  }

  public DomainException(Map<String, Object> userMessages, String message, Throwable cause) {
    super(message, cause);
    if(Objects.nonNull(userMessages)) {
      this.userMessages = userMessages;
    }
  }

  public DomainException(String userMessage) {
    super(userMessage);
    userMessages.put("error", userMessage);
  }

  public DomainException(Map<String, Object> userMessages) {
    super();
    if(Objects.nonNull(userMessages)) {
      this.userMessages = userMessages;
    }
  }

  public Map<String, Object> getUserMessages() {
    return userMessages;
  }

}
