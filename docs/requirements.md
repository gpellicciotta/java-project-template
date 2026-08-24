# Requirements

Functional and technical requirements for the `java-project-template` project.

---

## 1. High-Level Goals

- Provide a standardized, minimal, and fully configured starter project template for Java development using Gradle.
- Enable developers and AI assistants to bootstrap clean, production-ready Java projects adhering to cross-project development guidelines.
- Provide out-of-the-box support for unit testing with JUnit 5, JDK toolchain pinning, manifest-based versioning, and GitHub Actions CI.
- Supply an automated scaffolding tool (`create` subcommand) that clones and renames the template cleanly into new project repositories.

---

## 2. Functional Requirements

### 2.1. CLI Entry Point
- Provide a subcommand-driven CLI supporting standard commands:
  - `help` (and `--help`, `-h`): displays tool version, description, usage, and exit codes.
  - `version` (and `--version`): displays `{name} v{version} - {copyright}` in a single line.
  - `greet [name]`: sample business logic action demonstrating CLI-to-core dispatch.
  - `create <project-name> [-o <output-dir>]`: scaffolds a new project as a renamed copy of the template.
- Exit with code 0 on successful command completion or help/version invocation; exit with non-zero on errors.

### 2.2. Core Logic Separation
- Maintain clear separation between CLI parsing/IO (`myproject.Cli`), business logic (`myproject.Core`), and template scaffolding machinery (`myproject.Scaffold`).
- Ensure core business logic remains independent of console streams, exit calls, or CLI frameworks for seamless unit testing.

### 2.3. Project Scaffolding
- Automate repository renaming, directory structure restructuring, package name rewriting, and reset of version history (`CHANGELOG.md`, `TODO.md`, and `build.gradle`'s version).
- Prevent accidental overwrite when target directories already exist.

---

## 3. Technical Requirements

- **Java Version**: Explicitly targeted and compiled with Java 25 toolchain (`java.toolchain`).
- **Build System**: Gradle with pinned Gradle wrapper (`gradlew` / `gradlew.bat`).
- **Testing**: JUnit 5 Jupiter test framework with JUnit platform launcher.
- **Packaging**: Standard application plugin configuration generating runnable jar archives with manifest attributes (`Main-Class`, `Implementation-Title`, `Implementation-Version`, `Implementation-Vendor`).
- **Code Standards**: 2-space indentation, UTF-8 encoding, LF line endings, US English.

