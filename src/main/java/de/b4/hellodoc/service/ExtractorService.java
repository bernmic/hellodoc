package de.b4.hellodoc.service;

import de.b4.hellodoc.extractor.Extractor;
import de.b4.hellodoc.extractor.TextExtractor;
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
  }

  public Extractor getExtractor(String extension) {
    if (!extractors.containsKey(extension)) {
      throw new IllegalArgumentException("Unknown extension: " + extension);
    }
    return extractors.get(extension);
  }
}
