package reva.ide;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LanguageServerTestUtils {
  private LanguageServerTestUtils() {}

  /**
   * Get the file content as a Json object.
   *
   * @param filePath to read the content
   * @return {@link JsonObject}
   */
  public static JsonObject fileContentAsObject(Path filePath) {
    return JsonParser.parseString(getFileContent(filePath)).getAsJsonObject();
  }

  /**
   * Get the file content.
   *
   * @param filePath to read the content
   * @return {@link String}
   */
  public static String getFileContent(Path filePath) {
    String contentAsString = "";
    try {
      contentAsString = new String(Files.readAllBytes(filePath));
    } catch (IOException ex) {
      log.error(ex.getMessage());
    }

    return contentAsString;
  }

  public static List<String> getTestConfigs(Path testRoot, String configDir) {
    try {
      return Files.walk(testRoot.resolve(configDir))
          .filter(
              path -> {
                File file = path.toFile();
                return file.isFile() && file.getName().endsWith(".json");
              })
          .map(path -> path.toFile().getName())
          .collect(Collectors.toList());
    } catch (IOException e) {
      // If failed to load tests, then it's a failure
      Assert.fail("Unable to load test configs");
      return Collections.emptyList();
    }
  }

  public static void updateConfig(
      String actual, JsonObject configJson, Path testRoot, String configDir, String config) {
    configJson.addProperty("expected", actual);
    Path path = testRoot.resolve(configDir).resolve(config);
    try {
      Files.writeString(path, configJson.toString());
    } catch (IOException e) {
      // Ignore the file write
    }
  }
}
