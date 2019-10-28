package de.b4.hellodoc.service;

import de.b4.hellodoc.adapter.ElasticFulltextAdapter;
import de.b4.hellodoc.adapter.FulltextAdapter;
import de.b4.hellodoc.adapter.LuceneFulltextAdapter;
import de.b4.hellodoc.configuration.GlobalConfiguration;
import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.IndexData;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.b4.hellodoc.configuration.GlobalConfiguration.FULLTEXT_ELASTIC;
import static de.b4.hellodoc.configuration.GlobalConfiguration.FULLTEXT_LUCENE;

@ApplicationScoped
public class FulltextService {
  private final static Logger LOGGER = Logger.getLogger(FulltextService.class.getName());

  private static FulltextAdapter fulltextAdapter = null;

  @Inject
  GlobalConfiguration globalConfiguration;

  private void init() {
    if (FULLTEXT_LUCENE.equals(globalConfiguration.getFulltextType())) {
      fulltextAdapter = new LuceneFulltextAdapter();
      fulltextAdapter.init(globalConfiguration);
    }
    else if (FULLTEXT_ELASTIC.equals(globalConfiguration.getFulltextType())) {
      fulltextAdapter = new ElasticFulltextAdapter();
      fulltextAdapter.init(globalConfiguration);
    }
    else {
      fulltextAdapter = null;
      LOGGER.fatal("Fulltext engine is not configured.");
    }
  }

  public void addToIndex(IndexData data) throws IOException {
    if (fulltextAdapter == null) {
      init();
    }
    fulltextAdapter.addToIndex(data);
  }
  public void removeFromIndex(Document document) {
    if (fulltextAdapter == null) {
      init();
    }
    fulltextAdapter.removeFromIndex(document);
  }

  public List<Document> findDocuments(String text) {
    if (fulltextAdapter == null) {
      init();
    }
    return fulltextAdapter.findDocuments(text);
  }
}
