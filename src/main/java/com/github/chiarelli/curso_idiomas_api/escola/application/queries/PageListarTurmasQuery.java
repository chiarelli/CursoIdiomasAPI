package com.github.chiarelli.curso_idiomas_api.escola.application.queries;

import org.springframework.data.domain.Page;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.TurmaInterface;

import io.jkratz.mediator.core.Request;

public class PageListarTurmasQuery implements Request<Page<TurmaInterface>> {

  private final int page;
  private final int size;

  public PageListarTurmasQuery() {
    this.page = 1;
    this.size = 10;
  }
  
  public PageListarTurmasQuery(int page, int size) {
    this.page = page;
    this.size = size;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }
}
