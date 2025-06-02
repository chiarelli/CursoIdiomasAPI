package com.github.chiarelli.curso_idiomas_api.escola.application.queries;

import org.springframework.data.domain.Page;

import com.github.chiarelli.curso_idiomas_api.escola.domain.contracts.AlunoInterface;

import io.jkratz.mediator.core.Request;

public class PageListarAlunosQuery implements Request<Page<AlunoInterface>> {

  private final int page;
  private final int size;

  public PageListarAlunosQuery() {
    this.page = 1;
    this.size = 10;
  }
  
  public PageListarAlunosQuery(int page, int size) {
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
