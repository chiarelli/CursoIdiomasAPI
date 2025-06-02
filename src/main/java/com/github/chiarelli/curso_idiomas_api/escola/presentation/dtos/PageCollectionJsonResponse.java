package com.github.chiarelli.curso_idiomas_api.escola.presentation.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"page", "size", "length", "total_query_count", "total_pages", "content"})
public class PageCollectionJsonResponse<T> {

  private long length;

  private long page;

  private long size;

  @JsonProperty("total_query_count")
  private long queryCount;
  
  @JsonProperty("total_pages")
  private long totalPages;
  
  @JsonProperty("content")
  private List<T> data;

  // Construtor padrão
  public PageCollectionJsonResponse() {}

  // Construtor com parâmetros
  public PageCollectionJsonResponse(
    long length, 
    long page,
    long size,
    long queryCount, 
    long totalPages, 
    List<T> data) {
    this.length = length;
    this.page = page;
    this.size = size;
    this.queryCount = queryCount;
    this.totalPages = totalPages;
    setData(data);
  }

  public long getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public long getQueryCount() {
    return queryCount;
  }

  public void setQueryCount(int queryCount) {
    this.queryCount = queryCount;
  }

  public long getTotalPages() {
    return totalPages;
  }

  public void setPage(int page) {
    this.totalPages = page;
  }

  public long getPage() {
    return page;
  }

  public long getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
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
