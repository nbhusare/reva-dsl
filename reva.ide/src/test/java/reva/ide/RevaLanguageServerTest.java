package reva.ide;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Module;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.xtext.ide.server.concurrent.RequestManager;
import org.eclipse.xtext.testing.AbstractLanguageServerTest;
import org.eclipse.xtext.util.Modules2;
import org.eclipse.xtext.xbase.lib.Procedures;
import org.junit.Assert;
import org.junit.ComparisonFailure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base language server test implementation.
 * The language feature related test suits should extend from this class.
 */
public class RevaLanguageServerTest extends AbstractLanguageServerTest {

  protected final Path testRoot;
  protected final String configDir = "config";
  protected final String sourceDir = "source";
  protected boolean serverInitialized = false;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public RevaLanguageServerTest(String testRoot) {
    super("reva");
    this.testRoot = Paths.get("src/test/resources/").toAbsolutePath().resolve(testRoot);
  }

  protected InitializeResult initialize(
      final Procedures.Procedure1<? super InitializeParams> initializer) {
    // Allows to initialize the server only once
    if (serverInitialized) {
      return null;
    }
    serverInitialized = true;
    return this.initialize(initializer, true);
  }

  protected void updateConfig(String actual, JsonObject configJson, String config) {
    configJson.add("expected", JsonParser.parseString(actual));
    Path path = testRoot.resolve(configDir).resolve(config);
    try {
      Files.writeString(path, gson.toJson(configJson));
    } catch (IOException e) {
      // Ignore the file write
    }
  }

  protected List<String> testConfigs() {
    try {
      List<String> ignoredList = ignoredList();
      return Files.walk(this.testRoot.resolve(this.configDir))
          .filter(
              path -> {
                File file = path.toFile();
                return file.isFile()
                    && !ignoredList().contains(file.getName().replace(".json", ""))
                    && file.getName().endsWith(".json");
              })
          .map(path -> path.toFile().getName())
          .collect(Collectors.toList());
    } catch (IOException e) {
      // If failed to load tests, then it's a failure
      Assert.fail("Unable to load test configs");
      return Collections.emptyList();
    }
  }

  protected JsonObject getConfigJson(String config) {
    Path configJsonPath = testRoot.resolve(configDir).resolve(config);
    return LanguageServerTestUtils.fileContentAsObject(configJsonPath);
  }

  @Override
  protected Module getServerModule() {
    return Modules2.mixin(
        new LanguageServerModule() {
          @Override
          protected void configure() {
            super.configure();
            bind(RequestManager.class).to(DirectRequestManager.class);
          }
        });
  }

  /**
   * A list of test configs to be ignored.
   *
   * @return {@link List} of strings
   */
  protected List<String> ignoredList() {
    return Collections.emptyList();
  }

  public void assertEquals(String expected, String actual) {
    try {
      Assert.assertEquals(JsonParser.parseString(expected), JsonParser.parseString(actual));
    } catch (AssertionError e) {
      throw new ComparisonFailure("Comparison failure", expected, actual);
    }
  }

  protected String _toExpectation(final List<?> elements) {
    return gson.toJson(elements);
  }

  protected String _toExpectation(final Hover element) {
    return gson.toJson(element);
  }
}
