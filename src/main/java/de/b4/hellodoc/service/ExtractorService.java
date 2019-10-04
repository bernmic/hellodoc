package de.b4.hellodoc.service;

import de.b4.hellodoc.extractor.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ExtractorService {
  private final static Logger LOGGER = Logger.getLogger(ExtractorService.class.getName());

  private Map<String, Extractor> extractors;

  public ExtractorService() {
    LOGGER.info("Start ExtractorService");
    extractors = new HashMap<>();
    extractors.put("txt", new TextExtractor());
    extractors.put("pdf", new PdfExtractor());
    extractors.put("doc", new WordExctractor());
    extractors.put("docx", new XWordExtractor());
    extractors.put("xls", new ExcelExtractor());
    extractors.put("xlsx", new XExcelExtractor());
    extractors.put("ppt", new PowerpointExtractor());
    extractors.put("pptx", new XPowerpointExtractor());
    ImageExtractor imageExtractor = new ImageExtractor();
    // image formats
    extractors.put("jpeg", imageExtractor);
    extractors.put("jpg", imageExtractor);
    extractors.put("tiff", imageExtractor);
    extractors.put("tif", imageExtractor);
    extractors.put("png", imageExtractor);
    extractors.put("gif", imageExtractor);
    extractors.put("bmp", imageExtractor);
    extractors.put("webp", imageExtractor);
    extractors.put("ico", imageExtractor);
    extractors.put("pcx", imageExtractor);
    extractors.put("psd", imageExtractor);
    // camera raw formats
    extractors.put("cr2", imageExtractor);
    extractors.put("nef", imageExtractor);
    extractors.put("orf", imageExtractor);
    extractors.put("arw", imageExtractor);
    extractors.put("rw2", imageExtractor);
    extractors.put("rwl", imageExtractor);
    extractors.put("srw", imageExtractor);
    // multimedia formats
    extractors.put("wav", imageExtractor);
    extractors.put("avi", imageExtractor);
    extractors.put("mp4", imageExtractor);
  }

  public boolean supportsExtension(String extension) {
    return extractors.containsKey(extension);
  }

  public Extractor getExtractor(String extension) {
    if (!extractors.containsKey(extension)) {
      throw new IllegalArgumentException("Unknown extension: " + extension);
    }
    return extractors.get(extension);
  }

  public String[] getExtensions() {
    return extractors.keySet().toArray(new String[0]);
  }
}
