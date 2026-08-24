package myproject;

import java.io.PrintStream;

/**
 * Sample CLI for template-project. {@link #run} is the testable entry point (returns an exit code, never
 * calls {@link System#exit}); {@link #main} is the real process entry point.
 */
public final class Cli
{
  private Cli() { }

  public static String name() {
    String n = Cli.class.getPackage().getImplementationTitle();
    // Same jar-manifest-only caveat as version() below: Implementation-Title only resolves when running from
    // the built jar, not when running compiled classes directly (e.g. `gradlew run` or the test suite).
    return (n != null) ? n : "template-project";
  }

  public static String version() {
    String v = Cli.class.getPackage().getImplementationVersion();
    // Package.getImplementationVersion() only resolves when running from a jar with a manifest (e.g. the
    // installed distribution) - it's null when running compiled classes directly (e.g. `gradlew run`,
    // an IDE run configuration, or the test suite).
    return (v != null) ? v : "0.0.0+unknown";
  }

  public static String vendor() {
    String v = Cli.class.getPackage().getImplementationVendor();
    // Same jar-manifest-only caveat as version()/name(): Implementation-Vendor only resolves when running
    // from the built jar, not when running compiled classes directly (e.g. `gradlew run` or the test suite).
    return (v != null) ? v : "Giovanni Pellicciotta";
  }

  public static String versionLine() {
    return name() + " v" + version() + " - Copyright " + vendor();
  }

  public static String helpMessage(boolean verbose) {
    StringBuilder sb = new StringBuilder();
    sb.append(versionLine()).append("\n\n");
    sb.append("A minimal starter project and scaffolding CLI for Java and Gradle development.\n\n");
    sb.append("usage: ").append(name()).append(" <command> [options]\n\n");
    sb.append("commands:\n");
    sb.append("  help                              Show this usage info\n");
    sb.append("  version                           Show the current version\n");
    sb.append("  greet [name]                      Print a greeting (sample business logic; default: wereld)\n");
    sb.append("  create <project-name> [-o <dir>]  Create a new project as a renamed copy of this template\n\n");
    sb.append("options:\n");
    sb.append("  -h, --help                        Show this usage info\n");
    sb.append("  --version                         Show the current version\n");
    sb.append("  --verbose                         Show extended usage guidelines\n\n");
    if (verbose) {
      sb.append("details:\n");
      sb.append("  create:\n");
      sb.append("    Copies this template tree to <dir>/<project-name>, updates package declarations,\n");
      sb.append("    resets CHANGELOG.md and TODO.md, and initializes a fresh build.gradle version.\n\n");
    }
    sb.append("exit codes:\n");
    sb.append("  0  Success (including help and version display)\n");
    sb.append("  1  Error (invalid arguments or execution failure)\n");
    return sb.toString();
  }

  public static int run(String[] args, PrintStream out, PrintStream err) {
    if (args.length == 0) {
      out.print(helpMessage(false));
      return 0;
    }

    String firstArg = args[0];
    if ("help".equals(firstArg) || "--help".equals(firstArg) || "-h".equals(firstArg)) {
      boolean verbose = false;
      for (int i = 1; i < args.length; i++) {
        if ("--verbose".equals(args[i])) {
          verbose = true;
          break;
        }
      }
      out.print(helpMessage(verbose));
      return 0;
    }

    if ("version".equals(firstArg) || "--version".equals(firstArg)) {
      out.println(versionLine());
      return 0;
    }

    if ("greet".equals(firstArg)) {
      String name = (args.length > 1) ? args[1] : "wereld";
      out.println(Core.greet(name));
      return 0;
    }

    if ("create".equals(firstArg)) {
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

    err.println("error: unknown command '" + firstArg + "'");
    out.print(helpMessage(false));
    return 1;
  }

  public static void main(String[] args) {
    System.exit(run(args, System.out, System.err));
  }
}
