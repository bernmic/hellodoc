package de.b4.hellodoc.extractor;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XSLFSlideShow;
import org.jboss.logging.Logger;

import java.io.InputStream;

public class XPowerpointExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(XPowerpointExtractor.class.getName());
  @Override
  public String extractContent(InputStream is) {
    try {
      XSLFSlideShow document = new XSLFSlideShow(OPCPackage.open(is));
      XSLFPowerPointExtractor poiExtractor = new XSLFPowerPointExtractor(document);
      return poiExtractor.getText();
    } catch (Exception e) {
      LOGGER.warn("Error reading PPTX file", e);
    }
    return null;
  }
}
