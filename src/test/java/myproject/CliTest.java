package myproject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CliTest
{
  private static String runAndCaptureOut(String... args) throws Exception {
    ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
    ByteArrayOutputStream errBuf = new ByteArrayOutputStream();
    int exitCode = Cli.run(args, new PrintStream(outBuf, true, "UTF-8"), new PrintStream(errBuf, true, "UTF-8"));
    assertEquals(0, exitCode, "expected exit code 0, stderr was: " + errBuf);
    return outBuf.toString(StandardCharsets.UTF_8);
  }

  @Test
  void helpExitsZero() throws Exception {
    assertTrue(runAndCaptureOut("help").contains("usage:"));
  }

  @Test
  void noArgsShowsHelp() throws Exception {
    assertTrue(runAndCaptureOut().contains("usage:"));
  }

  @Test
  void version() throws Exception {
    assertFalse(runAndCaptureOut("version").isBlank());
  }

  @Test
  void greet() throws Exception {
    assertEquals("Hello, Gio", runAndCaptureOut("greet", "Gio").strip());
  }

  @Test
  void createScaffoldsRenamedProject(@TempDir Path tmpDir) throws Exception {
    String projectName = "sample-app";
    runAndCaptureOut("create", projectName, "-o", tmpDir.toString());

    Path destination = tmpDir.resolve(projectName);
    assertTrue(Files.isDirectory(destination));
    assertTrue(Files.isRegularFile(destination.resolve("src/main/java/sample_app/Cli.java")));
    // Deliberately not a literal "myproject" absence-check: this test file is itself copied (and rewritten) by
    // `create`, so a hardcoded reference to *this* template's own current package name would get swept up in
    // that same rewrite when a project scaffolded from this template later runs its own inherited copy of this
    // test - silently turning the check into an always-true no-op, or (as first written) into a check that
    // wrongly asserts the just-created package is *absent*. Assert the structural invariant instead: exactly
    // one package directory under src/main/java, regardless of what it's named.
    try (Stream<Path> children = Files.list(destination.resolve("src/main/java"))) {
      List<Path> packageDirs = children.filter(Files::isDirectory).toList();
      assertEquals(1, packageDirs.size(), "expected exactly one package directory, found: " + packageDirs);
    }

    String buildGradle = Files.readString(destination.resolve("build.gradle"), StandardCharsets.UTF_8);
    assertTrue(buildGradle.contains("version = '0.0.1'"));
    assertTrue(buildGradle.contains("mainClass = 'sample_app.Cli'"));

    String readme = Files.readString(destination.resolve("README.md"), StandardCharsets.UTF_8);
    assertTrue(readme.contains("Sample App"));

    String releases = Files.readString(destination.resolve("RELEASES.md"), StandardCharsets.UTF_8);
    assertTrue(releases.contains("Initial release of the Sample App project."));

    String todo = Files.readString(destination.resolve("TODO.md"), StandardCharsets.UTF_8);
    assertFalse(todo.contains("github.com/gpellicciotta"));
  }

  @Test
  void createRefusesExistingDestination(@TempDir Path tmpDir) throws Exception {
    String projectName = "dup-app";
    Files.createDirectory(tmpDir.resolve(projectName));

    ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
    ByteArrayOutputStream errBuf = new ByteArrayOutputStream();
    int exitCode = Cli.run(new String[]{"create", projectName, "-o", tmpDir.toString()},
        new PrintStream(outBuf, true, "UTF-8"), new PrintStream(errBuf, true, "UTF-8"));
    assertEquals(1, exitCode);
  }
}
