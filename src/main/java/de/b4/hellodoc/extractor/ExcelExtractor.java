package de.b4.hellodoc.extractor;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class ExcelExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(WordExctractor.class.getName());

  @Override
  public String extractContent(InputStream is) {
    try {
      POIFSFileSystem fs = new POIFSFileSystem(is);
      org.apache.poi.hssf.extractor.ExcelExtractor poiExtractor = new org.apache.poi.hssf.extractor.ExcelExtractor(fs);
      return poiExtractor.getText();
    } catch (IOException e) {
      LOGGER.warn("Failed to load XLS", e);
    }
    return null;
  }
}
