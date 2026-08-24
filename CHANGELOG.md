# Versioned Changes

A summarized overview of all changes, per version of this project.

> Entries will be added in reverse chronological order, so with the most recent at the top.
> 
> Status codes used are:
> - `[in development]` - actively being developed
> - `[{{date}}]` - frozen/finalized on {{date}}
> - `[released: {{date}}]` - released to package manager or production on {{date}}
> - `[broken]` - considered broken and not be used

---

## v1.0.0 [2026-08-24]
- Aligned template structure, documentation, and tooling with cross-project development guidelines:
  - Renamed `LICENSE` to `LICENSE.md` and `RELEASES.md` to `CHANGELOG.md` with version status tags.
  - Formatted `TODO.md` to follow milestone-based task tracking (`## Next Milestone`, `### Backlog`) per coordinating work guidelines.
  - Added `work/` to `.gitignore` to support parallel task worktrees.
  - Added mandatory documentation in `docs/`: `docs/requirements.md` (goals, functional and technical requirements), `docs/devops.md` (workflows, task protocol, release procedures), and updated `docs/index.md`.
  - Updated CLI (`Cli.java`) to adhere to CLI guidelines: formatted version output as `{name} v{version} - {copyright}`, added structured multi-line help output with exit codes, and supported `-h`, `--help`, `--version`, and `--verbose`.
  - Updated scaffolding engine (`Scaffold.java`) to emit `CHANGELOG.md`, `LICENSE.md`, and standard `TODO.md` when creating new projects.
  - Expanded test suite (`CliTest.java`) covering all CLI options, exit codes, and scaffolded project validation.
- Build: Fix `gradlew` missing its executable bit, which broke `./gradlew build` on Linux CI runners
- Build: Upgrade `actions/checkout` and `actions/setup-java` in CI to their current major versions

## v0.3.0 [2026-08-19]
- Added `docs/multi-module.md`, documenting the minimal `buildSrc` convention-plugin shape to reach for once a second Gradle module is needed.
- Added a `package-info.java` convention: non-obvious package design decisions are documented in the package's `package-info.java` rather than a README fragment (`src/main/java/myproject/package-info.java` worked example).
- The `version` command's copyright name is no longer hardcoded in `Cli.java`: it's read from the jar manifest's `Implementation-Vendor` stamped from `ext.vendor` in `build.gradle`.

## v0.2.0 [2026-08-19]
- `version` command prints `{project name} {version} - Copyright Giovanni Pellicciotta` instead of just the bare version number. The project name is read from the jar manifest's `Implementation-Title`.

## v0.1.0 [2026-08-19]
- First working version of the template: a minimal Gradle/Java project (`src/main/java` + `src/test/java` layout, JUnit 5, Gradle wrapper with pinned toolchain JDK).
- `Cli.java` gained `help`/`version`/`greet <name>`/`create <project-name> [-o <output-dir>]` subcommands.
- Added `Scaffold.java` to scaffold a new project as a renamed copy of this template.
- The version is stamped into the jar manifest and read back at runtime via `Package.getImplementationVersion()`.
- Added `.github/workflows/ci.yml`, `LICENSE.md`, and `docs/index.md`.
- Fixed self-referential package name assertion in `CliTest`.
