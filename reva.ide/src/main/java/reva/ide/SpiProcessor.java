package reva.ide;

import com.google.common.reflect.ClassPath;
import reva.ide.spi.RevaSpiService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpiProcessor {

  private static final Path servicesDir =
      Paths.get("src", "main", "resources", "META-INF", "services").toAbsolutePath();

  public static void main(String[] args) throws IOException {
    List<ClassPath.ClassInfo> classInfos =
        ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
            .filter(SpiProcessor::isSPI)
            .toList();

    if (Files.exists(servicesDir)) {
      deleteDirectory(servicesDir.toFile());
    }
    Files.createDirectory(servicesDir);

    writeToServiceFiles(classInfos);
  }

  private static void writeToServiceFiles(List<ClassPath.ClassInfo> classInfos) {
    Map<String, StringBuilder> contentMapping = new HashMap<>();

    for (ClassPath.ClassInfo classInfo : classInfos) {
      String spiName = getSPIName(classInfo.load());
      String className = classInfo.getName();

      if (contentMapping.containsKey(spiName)) {
        contentMapping.get(spiName).append(System.lineSeparator()).append(className);
      } else {
        contentMapping.put(spiName, new StringBuilder(className));
      }
    }

    contentMapping.forEach(
        (spi, content) -> {
          try {
            Files.writeString(servicesDir.resolve(spi), content.toString());
          } catch (IOException e) {
            throw new RuntimeException("Failed to generate the SPI service files");
          }
        });
  }

  private static String getSPIName(Class<?> cls) {
    return Arrays.stream(cls.getAnnotations())
        .filter(annotation -> annotation.annotationType().equals(RevaSpiService.class))
        .map(annotation -> ((RevaSpiService) annotation).value())
        .findFirst()
        .orElse("");
  }

  private static boolean isSPI(ClassPath.ClassInfo classInfo) {
    String packageName = SpiProcessor.class.getPackageName();

    return classInfo.getPackageName().startsWith(packageName)
        && Arrays.stream(classInfo.load().getAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(RevaSpiService.class));
  }

  private static void deleteDirectory(File file) {
    File[] allContent = file.listFiles();

    if (allContent != null) {
      for (File fileItem : allContent) {
        deleteDirectory(fileItem);
      }
    }

    file.delete();
  }
}
