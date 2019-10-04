package de.b4.hellodoc.configuration;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.StringSubstitutor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class GlobalConfiguration {
  private static final Logger LOGGER = Logger.getLogger(ApiConfiguration.class.getName());
  private final static String INPUT_DIR = "input";
  private final static String ARCHIVE_DIR = "archive";
  private final static String INDEX_DIR = "index";

  private final StringSubstitutor stringSubstitutor;
  private String homeDir;
  private String indexDir;
  private String inputDir;
  private String archiveDir;

  @ConfigProperty(name = "hellodoc.category.uncategorizes", defaultValue = "Uncategorized")
  String uncategorizedName;

  @ConfigProperty(name = "hellodoc.data", defaultValue = "${home}/.hellodoc")
  String home;

  public GlobalConfiguration() {
    Map<String,String> substitutions;
    substitutions = new HashMap<>();
    substitutions.put("home", System.getProperty("user.home"));
    substitutions.put("temp", System.getProperty("java.io.tmpdir"));
    stringSubstitutor = new StringSubstitutor(substitutions);
  }

  public String getHomeDir() {
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

  public String getInputDir() {
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

  public String getArchiveDir() {
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

  public String getIndexDir() {
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

  public String getUncategorizedName() {
    return uncategorizedName;
  }
}
