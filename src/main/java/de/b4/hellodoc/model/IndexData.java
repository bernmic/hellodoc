package de.b4.hellodoc.model;

import java.time.LocalDateTime;

public class IndexData {
  private Document document;
  private String content;

  public IndexData(Document document, String content) {
    this.document = document;
    this.content = content;
  }

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
