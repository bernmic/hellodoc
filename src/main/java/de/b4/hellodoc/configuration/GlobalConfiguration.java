package de.b4.hellodoc.configuration;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StringSubstitutor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class GlobalConfiguration {
  private static final Logger LOGGER = Logger.getLogger(GlobalConfiguration.class.getName());
  private final static String INPUT_DIR = "input";
  private final static String ARCHIVE_DIR = "archive";
  private final static String INDEX_DIR = "index";

  public final static String FULLTEXT_LUCENE = "lucene";
  public final static String FULLTEXT_ELASTIC = "elastic";

  private StringSubstitutor stringSubstitutor;
  private String homeDir;
  private String indexDir;
  private String inputDir;
  private String archiveDir;

  @ConfigProperty(name = "hellodoc.data", defaultValue = "${home}/.hellodoc")
  String home;

  @ConfigProperty(name = "hellodoc.fulltext.type", defaultValue = FULLTEXT_LUCENE)
  String fulltextType;

  private void init() {
    LOGGER.debug("GlobalComfiguration initialized.....................................................");
    Map<String,String> substitutions;
    substitutions = new HashMap<>();
    substitutions.put("home", System.getProperty("user.home"));
    substitutions.put("temp", System.getProperty("java.io.tmpdir"));
    stringSubstitutor = new StringSubstitutor(substitutions);
  }

  @Produces
  public String getHomeDir() {
    if (stringSubstitutor == null) {
      init();
    }
    if (home == null) {
      LOGGER.error("Something went wrong. \"home\" is null. WTF.");
      home = "${home}/.hellodoc";
      LOGGER.warn("Set home to " + stringSubstitutor.replace(home));
    }
    if (homeDir == null) {
      homeDir = FilenameUtils.separatorsToUnix(home);
      homeDir = stringSubstitutor.replace(home);
      if (Files.notExists(Paths.get(homeDir))) {
        try {
          Files.createDirectories(Paths.get(homeDir));
        } catch (IOException e) {
          LOGGER.error("Error creating home directory", e);
        }
      }
    }
    return homeDir;
  }

  @Produces
  public String getInputDir() {
    if (stringSubstitutor == null) {
      init();
    }
    if (inputDir == null) {
      Path path = Paths.get(getHomeDir(), INPUT_DIR);
      if (Files.notExists(path)) {
        try {
          Files.createDirectories(path);
        } catch (IOException e) {
          LOGGER.error("Error creating input directory", e);
        }
      }
      inputDir = path.toString();
    }
    return inputDir;
  }

  @Produces
  public String getArchiveDir() {
    if (stringSubstitutor == null) {
      init();
    }
    if (archiveDir == null) {
      Path path = Paths.get(getHomeDir(), ARCHIVE_DIR);
      if (Files.notExists(path)) {
        try {
          Files.createDirectories(path);
        } catch (IOException e) {
          LOGGER.error("Error creating archive directory", e);
        }
      }
      archiveDir = path.toString();
    }
    return archiveDir;
  }

  @Produces
  public String getIndexDir() {
    if (stringSubstitutor == null) {
      init();
    }
    if (indexDir == null) {
      Path path = Paths.get(getHomeDir(), INDEX_DIR);
      if (Files.notExists(path)) {
        try {
          Files.createDirectories(path);
        } catch (IOException e) {
          LOGGER.error("Error creating index directory", e);
        }
      }
      indexDir = path.toString();
    }
    return indexDir;
  }

  @Produces
  public String getFulltextType() {
    return fulltextType;
  }
}
