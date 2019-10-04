package de.b4.hellodoc.service;

import de.b4.hellodoc.configuration.GlobalConfiguration;
import de.b4.hellodoc.model.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LuceneFulltextService implements FulltextService {
  private final static Logger LOGGER = Logger.getLogger(LuceneFulltextService.class.getName());

  private static final String FIELD_NAME = "name";
  private static final String FIELD_PATH = "path";
  private static final String FIELD_ID = "id";
  private static final String FIELD_CONTENT = "content";
  private static final int MAX_HITS = 10000;

  private StandardAnalyzer analyzer;
  private Directory directory;

  @Inject
  GlobalConfiguration globalConfiguration;

  public void init() {
    analyzer = new StandardAnalyzer();
    try {
      directory = new NIOFSDirectory(Paths.get(globalConfiguration.getIndexDir()));
    } catch (IOException e) {
      LOGGER.error("Error creating lucene index", e);
    }
  }

  private Directory getDirectory() {
    if (directory == null) {
      init();
    }
    return directory;
  }

  public void addToIndex(Document document, String content) throws IOException {
      IndexWriter indexWriter = new IndexWriter(getDirectory(), new IndexWriterConfig(analyzer));
      org.apache.lucene.document.Document luceneDocument = new org.apache.lucene.document.Document();
      luceneDocument.add(new TextField(FIELD_NAME, document.name, Field.Store.YES));
      luceneDocument.add(new TextField(FIELD_PATH, document.path, Field.Store.YES));
      luceneDocument.add(new LongPoint(FIELD_ID, document.id));
      luceneDocument.add(new StoredField(FIELD_ID, document.id));
      luceneDocument.add(new TextField(FIELD_CONTENT, content, Field.Store.NO));
      indexWriter.addDocument(luceneDocument);
      indexWriter.close();
  }

  public void removeFromIndex(Document document) {
    try {
      Term term = new Term(FIELD_ID, document.id.toString());
      IndexWriter indexWriter = new IndexWriter(getDirectory(), new IndexWriterConfig(analyzer));
      indexWriter.deleteDocuments(term);
    } catch (IOException e) {
      LOGGER.error("Error removing lucene document with id=" + document.id, e);
    }
  }

  public List<Document> findDocuments(String text) {
    List<Document> result = new ArrayList<>();
    try {
      Query query = new QueryParser(FIELD_CONTENT, analyzer).parse(text);
      IndexReader reader = DirectoryReader.open(directory);
      IndexSearcher searcher = new IndexSearcher(reader);
      TopDocs documents = searcher.search(query, MAX_HITS);
      for (ScoreDoc scoreDoc : documents.scoreDocs) {
        org.apache.lucene.document.Document luceneDocument = searcher.doc(scoreDoc.doc);
        long id = Long.parseLong(luceneDocument.get(FIELD_ID));
        Document document = Document.findById(id);
        if (document == null) {
          document = new Document();
          document.id = id;
          document.name = luceneDocument.get(FIELD_NAME);
          document.path = luceneDocument.get(FIELD_PATH);
        }
        result.add(document);
      }
    } catch (ParseException e) {
      LOGGER.warn("Error parsing query", e);
    } catch (IOException e) {
      LOGGER.warn("Error querying", e);
    }
    return result;
  }
}
