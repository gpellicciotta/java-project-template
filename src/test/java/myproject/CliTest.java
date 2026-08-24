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

  private static int runAndGetExitCode(ByteArrayOutputStream outBuf, ByteArrayOutputStream errBuf, String... args) throws Exception {
    return Cli.run(args, new PrintStream(outBuf, true, "UTF-8"), new PrintStream(errBuf, true, "UTF-8"));
  }

  @Test
  void helpExitsZero() throws Exception {
    String out = runAndCaptureOut("help");
    assertTrue(out.contains("usage:"));
    assertTrue(out.contains("exit codes:"));
    assertTrue(out.contains("commands:"));
    assertTrue(out.contains("options:"));
  }

  @Test
  void helpShortOptionExitsZero() throws Exception {
    String out = runAndCaptureOut("-h");
    assertTrue(out.contains("usage:"));
    assertTrue(out.contains("exit codes:"));
  }

  @Test
  void helpLongOptionExitsZero() throws Exception {
    String out = runAndCaptureOut("--help");
    assertTrue(out.contains("usage:"));
    assertTrue(out.contains("exit codes:"));
  }

  @Test
  void helpVerboseOptionIncludesDetails() throws Exception {
    String out = runAndCaptureOut("help", "--verbose");
    assertTrue(out.contains("details:"));
    assertTrue(out.contains("create:"));
  }

  @Test
  void noArgsShowsHelp() throws Exception {
    String out = runAndCaptureOut();
    assertTrue(out.contains("usage:"));
    assertTrue(out.contains("exit codes:"));
  }

  @Test
  void versionCommand() throws Exception {
    String out = runAndCaptureOut("version").strip();
    assertTrue(out.startsWith("template-project v"));
    assertTrue(out.contains("Copyright Giovanni Pellicciotta"));
  }

  @Test
  void versionOption() throws Exception {
    String out = runAndCaptureOut("--version").strip();
    assertTrue(out.startsWith("template-project v"));
    assertTrue(out.contains("Copyright Giovanni Pellicciotta"));
  }

  @Test
  void greet() throws Exception {
    assertEquals("Hello, Gio", runAndCaptureOut("greet", "Gio").strip());
  }

  @Test
  void greetDefaultName() throws Exception {
    assertEquals("Hello, wereld", runAndCaptureOut("greet").strip());
  }

  @Test
  void unknownCommandReturnsOne() throws Exception {
    ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
    ByteArrayOutputStream errBuf = new ByteArrayOutputStream();
    int exitCode = runAndGetExitCode(outBuf, errBuf, "unknown-cmd");
    assertEquals(1, exitCode);
    assertTrue(errBuf.toString(StandardCharsets.UTF_8).contains("error: unknown command 'unknown-cmd'"));
    assertTrue(outBuf.toString(StandardCharsets.UTF_8).contains("usage:"));
  }

  @Test
  void createScaffoldsRenamedProject(@TempDir Path tmpDir) throws Exception {
    String projectName = "sample-app";
    runAndCaptureOut("create", projectName, "-o", tmpDir.toString());

    Path destination = tmpDir.resolve(projectName);
    assertTrue(Files.isDirectory(destination));
    assertTrue(Files.isRegularFile(destination.resolve("src/main/java/sample_app/Cli.java")));

    try (Stream<Path> children = Files.list(destination.resolve("src/main/java"))) {
      List<Path> packageDirs = children.filter(Files::isDirectory).toList();
      assertEquals(1, packageDirs.size(), "expected exactly one package directory, found: " + packageDirs);
    }

    String buildGradle = Files.readString(destination.resolve("build.gradle"), StandardCharsets.UTF_8);
    assertTrue(buildGradle.contains("version = '0.0.1'"));
    assertTrue(buildGradle.contains("mainClass = 'sample_app.Cli'"));

    String readme = Files.readString(destination.resolve("README.md"), StandardCharsets.UTF_8);
    assertTrue(readme.contains("Sample App"));

    assertTrue(Files.isRegularFile(destination.resolve("LICENSE.md")));

    String changelog = Files.readString(destination.resolve("CHANGELOG.md"), StandardCharsets.UTF_8);
    assertTrue(changelog.contains("Initial release of the Sample App project."));

    String todo = Files.readString(destination.resolve("TODO.md"), StandardCharsets.UTF_8);
    assertTrue(todo.contains("## Next Milestone"));
    assertTrue(todo.contains("### Backlog"));

    assertTrue(Files.isRegularFile(destination.resolve("docs/index.md")));
    assertTrue(Files.isRegularFile(destination.resolve("docs/requirements.md")));
    assertTrue(Files.isRegularFile(destination.resolve("docs/devops.md")));
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
