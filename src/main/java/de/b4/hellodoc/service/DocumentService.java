package de.b4.hellodoc.service;

import de.b4.hellodoc.configuration.GlobalConfiguration;
import de.b4.hellodoc.model.Category;
import de.b4.hellodoc.model.Document;
import de.b4.hellodoc.model.DocumentType;
import io.quarkus.scheduler.Scheduled;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@ApplicationScoped
public class DocumentService {
  private final static Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

  @Inject
  GlobalConfiguration globalConfiguration;

  @Inject
  ExtractorService extractorService;

  @Inject
  LuceneFulltextService fulltextService;

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
    if (!extractorService.supportsExtension(extension)) {
      LOGGER.info("Unsupported document type " + path.toString());
      return null;
    }
    // get the basename of the file
    String basename = FilenameUtils.getBaseName(path.toString());
    // get the relative path inside the input folder
    Path relativePath = Paths.get(globalConfiguration.getInputDir()).relativize(path).getParent();
    String subDirectory = relativePath == null ? "" : relativePath.toString();
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
    String categoryName = subDirectory.equals("") ? globalConfiguration.getUncategorizedName() : subDirectory;
    Category category = Category.find("name", categoryName).firstResult();
    if (category == null) {
      category = new Category();
      category.name = categoryName;
      category.description = "Created while import";
      category.persistAndFlush();
    }
    // add document to database
    // first check if document is in database
    if (Document.find("path", targetFile.toString()).firstResult() != null) {
      LOGGER.warnf("Document with path %s exists. Stopping here!", targetFile.toString());
    }
    Document document = addDocument(targetFile.toString(), basename, category);
    try {
      String content = extractorService.getExtractor(extension).extractContent(Files.newInputStream(targetFile));
      fulltextService.addToIndex(document, content);
    } catch (IOException e) {
      LOGGER.error("Error adding document to index", e);
    }
    return document;
  }

  @Scheduled(every = "60s")
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
}
