package de.b4.hellodoc.extractor;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class TextExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(TextExtractor.class.getName());

  @Override
  public String extractContent(InputStream is) {
    try {
      return IOUtils.toString(is, "UTF-8");
    } catch (IOException e) {
      LOGGER.warn("Unable to load text document: " + e.getMessage());
      LOGGER.debug("", e);
      e.printStackTrace();
    }
    return null;
  }
}
