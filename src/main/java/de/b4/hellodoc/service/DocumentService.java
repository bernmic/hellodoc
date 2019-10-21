package de.b4.hellodoc.service;

import de.b4.hellodoc.configuration.GlobalConfiguration;
import de.b4.hellodoc.model.Category;
import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.DocumentType;
import io.quarkus.tika.TikaContent;
import io.quarkus.tika.TikaParser;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@ApplicationScoped
public class DocumentService {
  private final static Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

  @Inject
  GlobalConfiguration globalConfiguration;

  @Inject
  LuceneFulltextService fulltextService;

  @Inject
  TikaParser parser;

  public Document addDocument(String path) {
    return addDocument(path, FilenameUtils.getBaseName(path), Category.findById(0));
  }

  public Document addDocument(String path, String name, Category category) {
    String extension = FilenameUtils.getExtension(path).toLowerCase();
    Document document = new Document();
    document.path = path;
    document.name = name;
    document.documentType = DocumentType.findById(extension);
    document.category = category;
    document.persist();
    return document;
  }

  @Transactional
  public Document importDocumentFromPath(Path path) {
    // Get the extension and check if supported or not
    String extension = FilenameUtils.getExtension(path.toString()).toLowerCase();
    // get the basename of the file
    String basename = FilenameUtils.getBaseName(path.toString());
    // get the relative path inside the input folder
    Path relativePath = Paths.get(globalConfiguration.getInputDir()).relativize(path).getParent();
    String subDirectory = relativePath == null ? "" : FilenameUtils.separatorsToUnix(relativePath.toString());
    LOGGER.infof("Try to process %s with extension %s and basename %s at path \"%s\"", path.toString(), extension, basename, subDirectory);

    Path targetPath = Paths.get(globalConfiguration.getArchiveDir(), subDirectory);
    Path targetFile = Paths.get(targetPath.toString(), basename + "." + extension);
    // check if the file exists
    if (Files.exists(targetFile)) {
      LOGGER.warnf("File %s exists. Stopping here!", targetFile.toString());
      return null;
    }
    // move to the archive path
    LOGGER.infof("Move %s to %s", path.toString(), targetFile.toString());
    if (Files.notExists(targetPath)) {
      try {
        Files.createDirectories(targetPath);
      } catch (IOException e) {
        LOGGER.error("Error creating target path " + targetPath.toString(), e);
        return null;
      }
    }
    try {
      Files.copy(path, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    } catch (IOException e) {
      LOGGER.error("Error moving file " + targetPath.toString(), e);
      return null;
    }
    // get or create the category
    Category category = getOrCreateCategory(subDirectory);
    // add document to database
    // first check if document is in database
    if (Document.find("path", targetFile.toString()).firstResult() != null) {
      LOGGER.warnf("Document with path %s exists. Stopping here!", targetFile.toString());
    }
    Document document = addDocument(targetFile.toString(), basename, category);
    try {
      TikaContent tikaContent = parser.parse(Files.newInputStream(targetFile));
      String docType = tikaContent.getMetadata().getSingleValue("Content-Type");
      if (docType != null) {
        LOGGER.infof("Document has mimetype %s", docType);
        DocumentType dt = DocumentType.find("mimetype", docType).firstResult();
        if (dt != null) {
          LOGGER.infof("Found mimetype %s in database.", docType);
        }
      }
      String content = tikaContent.getText();
      fulltextService.addToIndex(document, content);
    } catch (IOException e) {
      LOGGER.error("Error adding document to index", e);
    }
    return document;
  }

  public void scanInputDirectory() {
    LOGGER.info("Start scanning input directory " + globalConfiguration.getHomeDir());
    Path home = Paths.get(globalConfiguration.getHomeDir());
    Path inputPath = Paths.get(globalConfiguration.getInputDir());
    try {
      Files.walkFileTree(inputPath, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (!attrs.isDirectory()) {
            importDocumentFromPath(file);
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      LOGGER.error("Error scanning input directory", e);
    }
  }

  private Category getOrCreateCategory(String subDirectory) {
    if (subDirectory == null || subDirectory.equals("")) {
      return null;
    }
    Category category = Category.find("name", subDirectory).firstResult();
    if (category == null) {
      category = new Category();
      category.name = subDirectory;
      category.description = "Created while import";
      category.persistAndFlush();
    }
    return category;
  }
}
