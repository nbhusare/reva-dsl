package reva.ide.contentassist;

import com.google.gson.JsonObject;
import org.eclipse.xtext.testing.TestCompletionConfiguration;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.lib.Procedures;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import reva.ide.LanguageServerTestUtils;
import reva.ide.RevaLanguageServerTest;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ContentAssistTest extends RevaLanguageServerTest {
  public ContentAssistTest() {
    super("contentassist");
  }

  @Test
  public void testCompletion() {
    List<String> failedTests = new ArrayList<>();
    for (String config : testConfigs()) {
      JsonObject configJson = getConfigJson(config);
      String source = configJson.get("source").getAsString();
      String expected = configJson.get("expected").toString();
      JsonObject cursor = configJson.get("cursor").getAsJsonObject();

      Path sourcePath = testRoot.resolve(sourceDir).resolve(source);
      String sourceContent = LanguageServerTestUtils.getFileContent(sourcePath);
      Procedures.Procedure1<TestCompletionConfiguration> procedure =
          p -> {
            p.setModel(sourceContent);
            p.setFilePath(sourcePath.toUri().toString());
            p.setLine(cursor.get("line").getAsInt());
            p.setColumn(cursor.get("col").getAsInt());
            p.setExpectedCompletionItems(expected);
          };

      try {
        testCompletion(procedure);
      } catch (ComparisonFailure e) {
        failedTests.add(config);
         updateConfig(e.getActual(), configJson.deepCopy(), config);
      }
    }
    boolean success = failedTests.isEmpty();
    String message;
    if (success) {
      message = "Content Assist tests passed successfully";
    } else {
      message = "Content Assist tests failed for: [" + Strings.concat(", ", failedTests) + "]";
    }
    Assert.assertTrue(message, success);
  }
}
