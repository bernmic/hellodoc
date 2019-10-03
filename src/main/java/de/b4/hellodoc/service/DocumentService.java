package de.b4.hellodoc.service;

import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.DocumentType;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class DocumentService {
  private final static Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

  @Inject
  ExtractorService extractorService;

  public Document addDocument(String path) {
    return addDocument(path, FilenameUtils.getBaseName(path));
  }

  @Transactional
  public Document addDocument(String path, String name) {
    String extension = FilenameUtils.getExtension(path).toLowerCase();
    Document document = new Document();
    document.path = path;
    document.name = name;
    document.documentType = DocumentType.findById(extension);
    document.persist();
    return document;
  }
}
