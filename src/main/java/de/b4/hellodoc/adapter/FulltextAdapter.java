package de.b4.hellodoc.adapter;

import de.b4.hellodoc.configuration.GlobalConfiguration;
import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.IndexData;

import java.io.IOException;
import java.util.List;

public interface FulltextAdapter {
  void init(GlobalConfiguration globalConfiguration);
  void addToIndex(IndexData data) throws IOException;
  void removeFromIndex(Document document);
  List<Document> findDocuments(String text) ;
}
