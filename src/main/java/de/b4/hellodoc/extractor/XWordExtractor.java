package de.b4.hellodoc.extractor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class XWordExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(XWordExtractor.class.getName());
  @Override
  public String extractContent(InputStream is) {
    try {
      XWPFDocument document = new XWPFDocument(OPCPackage.open(is));
      XWPFWordExtractor poiExtractor = new XWPFWordExtractor(document);
      return poiExtractor.getText();
    } catch (IOException e) {
      LOGGER.warn("Error reading file", e);
    } catch (InvalidFormatException e) {
      LOGGER.warn("Illegal file format", e);
    }
    return null;
  }
}
