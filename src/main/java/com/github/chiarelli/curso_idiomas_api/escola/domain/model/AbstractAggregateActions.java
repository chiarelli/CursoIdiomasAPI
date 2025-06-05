package com.github.chiarelli.curso_idiomas_api.escola.domain.model;

import java.util.Objects;

import com.github.chiarelli.curso_idiomas_api.escola.domain.InstanceValidator;

import io.jkratz.mediator.core.Mediator;
import jakarta.persistence.Transient;

public abstract class AbstractAggregateActions {

  @Transient
  protected Mediator mediator;
  
  @Transient
  protected InstanceValidator validator;

  protected AbstractAggregateActions(Mediator mediator, InstanceValidator validator) {
    Objects.requireNonNull(mediator);
    Objects.requireNonNull(validator);
    this.mediator = mediator;
    this.validator = validator;
  }

}
