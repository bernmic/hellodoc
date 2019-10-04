package de.b4.hellodoc.extractor;

import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class WordExctractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(WordExctractor.class.getName());
  @Override
  public String extractContent(InputStream is) {
    try {
      org.apache.poi.hwpf.extractor.WordExtractor poiExtractor = new org.apache.poi.hwpf.extractor.WordExtractor(is);
      return poiExtractor.getText();
    } catch (IOException e) {
      LOGGER.warn("Failed to load DOC", e);
    }
    return null;

  }
}
