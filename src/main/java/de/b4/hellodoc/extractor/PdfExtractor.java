package de.b4.hellodoc.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class PdfExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(PdfExtractor.class.getName());
  @Override
  public String extractContent(InputStream is) {
    try {
      PDDocument document = PDDocument.load(is);
      document.getClass();
      if( !document.isEncrypted() ){
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition( true );
        PDFTextStripper textStripper = new PDFTextStripper();
        return textStripper.getText(document);
      }
    } catch (IOException e) {
      LOGGER.warn("Failed to load PDF", e);
    }
    return null;
  }
}
