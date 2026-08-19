package myproject;

import java.io.PrintStream;

/**
 * Sample CLI for template-project. {@link #run} is the testable entry point (returns an exit code, never
 * calls {@link System#exit}); {@link #main} is the real process entry point.
 */
public final class Cli
{
  private Cli() { }

  private static final String USAGE = String.join("\n",
      "usage: template-project <command> [args]",
      "",
      "commands:",
      "  help                              Show this usage info",
      "  version                           Show the current version",
      "  greet [name]                      Print a greeting (sample business logic; default name: wereld)",
      "  create <project-name> [-o <dir>]  Create a new project as a renamed copy of this template",
      ""
  );

  public static String version() {
    String v = Cli.class.getPackage().getImplementationVersion();
    // Package.getImplementationVersion() only resolves when running from a jar with a manifest (e.g. the
    // installed distribution) - it's null when running compiled classes directly (e.g. `gradlew run`,
    // an IDE run configuration, or the test suite). Mirrors the Python template's
    // `except PackageNotFoundError: __version__ = "0.0.0+unknown"` fallback.
    return (v != null) ? v : "0.0.0+unknown";
  }

  public static int run(String[] args, PrintStream out, PrintStream err) {
    if ((args.length == 0) || "help".equals(args[0]) || "--help".equals(args[0])) {
      out.print(USAGE);
      return 0;
    }
    if ("version".equals(args[0]) || "--version".equals(args[0])) {
      out.println(version());
      return 0;
    }
    if ("greet".equals(args[0])) {
      String name = (args.length > 1) ? args[1] : "wereld";
      out.println(Core.greet(name));
      return 0;
    }
    if ("create".equals(args[0])) {
      if (args.length < 2) {
        err.println("error: 'create' requires a project-name argument");
        return 1;
      }
      String projectName = args[1];
      String outputDir = ".";
      for (int i = 2; i < args.length; i++) {
        if (("-o".equals(args[i]) || "--output-dir".equals(args[i])) && (i + 1 < args.length)) {
          outputDir = args[++i];
        }
      }
      try {
        java.nio.file.Path destination = Scaffold.createProject(projectName, outputDir);
        out.println("Created new project at " + destination);
        return 0;
      } catch (Scaffold.ScaffoldException e) {
        err.println("error: " + e.getMessage());
        return 1;
      }
    }
    out.print(USAGE);
    return 1;
  }

  public static void main(String[] args) {
    System.exit(run(args, System.out, System.err));
  }
}
