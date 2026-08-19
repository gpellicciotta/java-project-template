# Java Template Project

A minimal example project to get Java/Gradle development started quickly using the standard `src/main/java` +
`src/test/java` layout.

Includes JUnit 5 for testing, the Gradle wrapper (pinned Gradle + JDK toolchain versions), a GitHub Actions CI
workflow, and the project's docs/versioning conventions (`RELEASES.md`, `TODO.md`, `CLAUDE.md`) — see `CLAUDE.md`
for the details Claude Code reads to follow them automatically.

## Directory Layout

```
java-template-project/
  src/
    main/java/myproject/
      Core.java             # business logic
      Cli.java              # entry point (registered in build.gradle's application {mainClass})
      Scaffold.java         # `create` subcommand: copies + renames this template into a new project
    test/java/myproject/
      CoreTest.java
      CliTest.java
  docs/                     # other documentation (design notes, detailed plans) - created as needed
  build/                    # generated Gradle output; ignored by Git
    classes/                # compiled main and test classes
    generated/              # generated source and header files
    libs/                   # built jar and distribution archives
    reports/                # HTML test and Gradle reports
    resources/              # processed main and test resources
    scripts/                # generated start scripts
    test-results/           # machine-readable test results
    tmp/                    # temporary build state
  .github/
    workflows/
      ci.yml                # gradlew build (compile + test + jar), on push/PR
  gradle/
    wrapper/                # Gradle wrapper jar + properties (pinned Gradle version)
    gradle-daemon-jvm.properties  # pinned toolchain JDK version for the Gradle daemon itself
  gradlew / gradlew.bat
  LICENSE
  build.gradle              # project metadata, version, JDK toolchain, JUnit 5, application plugin, jar manifest
  settings.gradle           # rootProject.name
  .gitignore
  .editorconfig             # indent/charset/line-ending settings
  setup.ps1                 # one-shot bootstrap: git init, gradlew build
  CLAUDE.md
  RELEASES.md               # version history - top-level, all-uppercase
  TODO.md                   # prioritized backlog - top-level, all-uppercase
```

`build/` is created by Gradle and should not be committed. Delete it with `Remove-Item -Recurse -Force
build` when a clean rebuild is needed; the next Gradle command recreates the required directories.

Java uses the standard `src/main/java/{{package}}` + `src/test/java/{{package}}` layout (the Maven/Gradle
default, and what every Java IDE expects out of the box).

## Quick Start (Windows PowerShell)

Open PowerShell in the project root and run:

```powershell
. .\setup.ps1
```

This initializes a git repo (if one doesn't already exist) and runs `gradlew build`, which compiles the code,
runs the test suite, and produces a jar in `build/libs/`.

## Running Tests

```powershell
.\gradlew.bat test
```

## CLI

```powershell
java -jar build/libs/template-project-0.1.0.jar help
java -jar build/libs/template-project-0.1.0.jar version
java -jar build/libs/template-project-0.1.0.jar greet <name>
java -jar build/libs/template-project-0.1.0.jar create <project-name> [-o <output-dir>]
```

`create` scaffolds a new project as a copy of this template at `<output-dir>/<project-name>` (current directory
if `-o` is omitted), automating the renames described below in **Starting a new project from this template**.
Run it from within a checkout of this template (i.e. after `. .\setup.ps1` or `gradlew build` in this repo).
`create` locates the template root by walking up from wherever its own compiled class was loaded from.

For a development run without building or naming the jar, use `.\gradlew.bat run --args="<command> [args]"`.

## Building the Jar

```powershell
.\gradlew.bat build
```

The jar (with its version stamped into the manifest) lands in `build/libs/`.

## Releasing a Version

1. Choose the next semantic version, then update `version` in `build.gradle`. This is the single source of
  truth used for the jar name and manifest.
2. Add a dated entry for that version at the top of `RELEASES.md`.
3. Run the full verification and build:

  ```powershell
  .\gradlew.bat clean build
  ```

4. Commit the changes and create a matching annotated tag:

  ```powershell
  git add build.gradle RELEASES.md README.md
  git commit -m "Release v<version>"
  git tag -a v<version> -m "Release v<version>"
  git push origin HEAD --follow-tags
  ```

5. In GitHub, open **Releases**, choose **Draft a new release**, select the pushed `v<version>` tag, and add
  the release notes. Upload `build/libs/template-project-<version>.jar` under **Attach binaries**, then publish
  the release. The published GitHub Release is the versioned package distribution; CI continues to verify pushes
  and pull requests but does not publish artifacts automatically.

## Starting a new project from this template

Preferred: run `gradlew run --args="create <project-name> [-o <output-dir>]"` (see **CLI** above) — it does the
copy and every rename below for you.

To do it by hand instead: copy this folder, then rename every occurrence of the placeholder names below —
they're easy to miss because nothing enforces consistency between them, and a leftover mismatch (e.g. a
package directory not matching its own `package` declaration) breaks the build silently rather than loudly:

- `src/main/java/myproject/` and `src/test/java/myproject/` → `src/main/java/<your_package_name>/` and
  `src/test/java/<your_package_name>/` (and every `package myproject;`/`import myproject...` line inside them)
- `settings.gradle`'s `rootProject.name = 'template-project'`
- This README's title, and `CLAUDE.md`'s placeholder sections
- `RELEASES.md` — replace the `[Unreleased] v0.1.0` placeholder with your project's actual first entry once
  there's something real to release
- `build.gradle`'s `version` — reset to `0.0.1` (or your own starting point)

Then follow **Quick Start** above to verify the rename didn't break anything before writing real code.
