package de.b4.hellodoc.extractor;

import java.io.InputStream;

public interface Extractor {
  String extractContent(InputStream is);
}
