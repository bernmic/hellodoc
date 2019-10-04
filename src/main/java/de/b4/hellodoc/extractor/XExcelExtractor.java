package de.b4.hellodoc.extractor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class XExcelExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(XExcelExtractor.class.getName());
  @Override
  public String extractContent(InputStream is) {
    try {
      XSSFWorkbook document = new XSSFWorkbook(OPCPackage.open(is));
      XSSFExcelExtractor poiExtractor = new XSSFExcelExtractor(document);
      return poiExtractor.getText();
    } catch (IOException e) {
      LOGGER.warn("Error reading XLSX file", e);
    } catch (InvalidFormatException e) {
      LOGGER.warn("Illegal file format", e);
    }
    return null;
  }
}
