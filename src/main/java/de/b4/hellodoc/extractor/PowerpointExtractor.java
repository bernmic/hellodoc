package de.b4.hellodoc.extractor;

import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class PowerpointExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(PowerpointExtractor.class.getName());

  @Override
  public String extractContent(InputStream is) {
    try {
      PowerPointExtractor poiExtractor = new PowerPointExtractor(is);
      return poiExtractor.getText();
    } catch (IOException e) {
      LOGGER.warn("Failed to load PPT", e);
    }
    return null;
  }
}
