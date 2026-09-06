package myproject;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Minimal reference logging implementation, just enough to satisfy the cross-project logging guideline:
 * formatted log lines to stdout/stderr and an optional append-only log file, with {@code DEBUG} gated behind
 * an explicit opt-in. Deliberately a single global sink rather than a named-logger SPI (see
 * hinolugi-support.java's {@code logging} package for that) - grow into one only once a template-derived
 * project actually needs multiple back-ends or per-component loggers.
 */
public final class Log
{
  private Log() { }

  public enum Level { ERROR, WARN, INFO, DEBUG }

  private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static volatile boolean debugEnabled = false;
  private static volatile Path logFile = null;
  private static volatile PrintStream out = System.out;
  private static volatile PrintStream err = System.err;

  /** (Re)configure the sink; called once per {@code Cli.run} so state never leaks across invocations. */
  public static void configure(boolean debug, Path file, PrintStream outStream, PrintStream errStream) {
    debugEnabled = debug;
    logFile = file;
    out = outStream;
    err = errStream;
  }

  public static void error(String message) { write(Level.ERROR, null, message); }

  public static void error(String origin, String message) { write(Level.ERROR, origin, message); }

  public static void warn(String message) { write(Level.WARN, null, message); }

  public static void warn(String origin, String message) { write(Level.WARN, origin, message); }

  public static void info(String message) { write(Level.INFO, null, message); }

  public static void info(String origin, String message) { write(Level.INFO, origin, message); }

  public static void debug(String message) {
    if (debugEnabled) {
      write(Level.DEBUG, null, message);
    }
  }

  public static void debug(String origin, String message) {
    if (debugEnabled) {
      write(Level.DEBUG, origin, message);
    }
  }

  // Fixed-width (11 chars) so all four line up once followed by a single separating space.
  private static String indicator(Level level) {
    return switch (level) {
      case ERROR -> "**[ERROR]**";
      case WARN -> "**[WARN]** ";
      case INFO -> "**[INFO]** ";
      case DEBUG -> "**[DEBUG]**";
    };
  }

  // Continuation lines are indented to the column where the first line's message text started.
  private static String withIndentedContinuations(String prefix, String message) {
    if (message.indexOf('\n') < 0) {
      return prefix + message;
    }
    String[] lines = message.split("\n", -1);
    String continuationIndent = " ".repeat(prefix.length());
    StringBuilder sb = new StringBuilder(prefix).append(lines[0]);
    for (int i = 1; i < lines.length; i++) {
      sb.append('\n').append(continuationIndent).append(lines[i]);
    }
    return sb.toString();
  }

  private static synchronized void write(Level level, String origin, String message) {
    String originTag = (origin == null || origin.isEmpty()) ? "" : "<" + origin + "> ";

    // Console: no timestamp, and INFO carries no severity indicator at all.
    String consolePrefix = (level == Level.INFO) ? originTag : (indicator(level) + " " + originTag);
    PrintStream stream = (level == Level.ERROR || level == Level.WARN) ? err : out;
    stream.println(withIndentedContinuations(consolePrefix, message));

    Path file = logFile;
    if (file != null) {
      String timestamp = "[" + TIMESTAMP_FORMAT.format(LocalDateTime.now()) + "] ";
      String filePrefix = timestamp + indicator(level) + " " + originTag;
      String fileLine = withIndentedContinuations(filePrefix, message);
      try {
        Files.writeString(file, fileLine + System.lineSeparator(), StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      } catch (IOException e) {
        throw new UncheckedIOException("failed to append to log file " + file, e);
      }
    }
  }
}
