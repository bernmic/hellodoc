package de.b4.hellodoc.extractor;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jboss.logging.Logger;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class TikaExtractor implements Extractor {
    private final static Logger LOGGER = Logger.getLogger(TikaExtractor.class.getName());
    @Override
    public String extractContent(InputStream is) {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try {
            parser.parse(is, handler, metadata);
            LOGGER.info("Found format " + metadata.get(TikaCoreProperties.FORMAT));
            System.out.println(metadata);
            return handler.toString();
        } catch (Exception e) {
            LOGGER.error("Error importing file.", e);
        }
        return null;
    }
}
