package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageCollectionJsonResponse<T> {

  private int length;

  @JsonProperty("query_count")
  private int queryCount;
  
  private int page;
  
  private List<T> data;

  // Construtor padrão
  public PageCollectionJsonResponse() {}

  // Construtor com parâmetros
  public PageCollectionJsonResponse(int length, int queryCount, int page, List<T> data) {
    this.length = length;
    this.queryCount = queryCount;
    this.page = page;
    setData(data);
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getQueryCount() {
    return queryCount;
  }

  public void setQueryCount(int queryCount) {
    this.queryCount = queryCount;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public List<T> getData() {
    if(data == null) {
      data = new ArrayList<>();
    }
    return data;
  }

  public void setData(List<T> data) {
    if(Objects.nonNull(data)) {
      this.data = data;
    }
  }

}
