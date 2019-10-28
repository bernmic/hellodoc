package de.b4.hellodoc.adapter;

import de.b4.hellodoc.configuration.GlobalConfiguration;
import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.IndexData;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticFulltextAdapter implements FulltextAdapter {
  private final static Logger LOGGER = Logger.getLogger(ElasticFulltextAdapter.class.getName());

  private GlobalConfiguration globalConfiguration;

  @Override
  public void init(GlobalConfiguration globalConfiguration) {
    this.globalConfiguration = globalConfiguration;
  }

  @Override
  public void addToIndex(IndexData data) throws IOException {
    LOGGER.fatal("ElasticSearch is not implemented yet.");
  }

  @Override
  public void removeFromIndex(Document document) {
    LOGGER.fatal("ElasticSearch is not implemented yet.");
  }

  @Override
  public List<Document> findDocuments(String text) {
    LOGGER.fatal("ElasticSearch is not implemented yet.");
    return new ArrayList<>();
  }
}
