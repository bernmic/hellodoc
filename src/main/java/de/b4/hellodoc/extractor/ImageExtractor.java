package de.b4.hellodoc.extractor;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;

public class ImageExtractor implements Extractor {
  private final static Logger LOGGER = Logger.getLogger(ImageExtractor.class.getName());
  @Override
  public String extractContent(InputStream is) {
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(is);
      StringBuilder result = new StringBuilder();
      for (Directory directory : metadata.getDirectories()) {
        for (Tag tag : directory.getTags()) {
          result.append(String.format("%s\n", tag.toString()));
        }
      }
      return result.toString();
    } catch (ImageProcessingException e) {
      LOGGER.warn("Error processing image.", e);
    } catch (IOException e) {
      LOGGER.warn("IO Error with image", e);
    }
    return null;
  }
}
