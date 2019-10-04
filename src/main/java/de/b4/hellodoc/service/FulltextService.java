package de.b4.hellodoc.service;

import de.b4.hellodoc.model.Document;

import java.io.IOException;
import java.util.List;

public interface FulltextService {
  void addToIndex(Document document, String content) throws IOException;
  void removeFromIndex(Document document);
  List<Document> findDocuments(String text) ;
}
