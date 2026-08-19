# Release Notes

All notes will be in reverse chronological order.

## 2026-08-19 v0.3.0
- Added `docs/multi-module.md`, documenting the minimal `buildSrc` convention-plugin shape to reach for once a
  second Gradle module is needed (CLAUDE.md's "Development Guidelines" now links to it), based on a pattern
  survey of `hinolugi-counters`.
- Added a `package-info.java` convention: non-obvious package design decisions are documented in the package's
  `package-info.java` rather than a README fragment. `src/main/java/myproject/package-info.java` is the
  worked example (documents the `Cli`/`Core`/`Scaffold` split), and CLAUDE.md's Conventions section records
  the rule, based on a pattern survey of `hinolugi-support.java`.
- The `version` command's copyright name is no longer hardcoded in `Cli.java`: it's read from the jar
  manifest's `Implementation-Vendor` (`Cli.java`'s new `vendor()`, mirroring `name()`/`version()`), stamped
  from a new single-source-of-truth `ext.vendor` in `build.gradle`.

## 2026-08-19 v0.2.0
- `version` command now prints `{project name} {version} - Copyright Giovanni Pellicciotta` instead of just
  the bare version number. The project name is read from the jar manifest's `Implementation-Title`
  (`Cli.java`'s new `name()`, mirroring `version()`'s `Implementation-Version` lookup), falling back to
  `"template-project"` when run from compiled classes rather than a jar.

## 2026-08-19 v0.1.0
- First working version of the template: a minimal Gradle/Java project (`src/main/java`+`src/test/java`
  layout, JUnit 5, the Gradle wrapper with a pinned toolchain JDK) plus the `RELEASES.md`/`TODO.md`/`CLAUDE.md`
  docs/versioning conventions defined by this project.
- `Cli.java` gained `help`/`version`/`greet <name>`/`create <project-name> [-o <output-dir>]` subcommands,
  `create` scaffolds a new project as a renamed copy of this template (`Scaffold.java`).
- The version is stamped into the jar manifest and read back at runtime via
  `Package.getImplementationVersion()` rather than hardcoded — `build.gradle`'s `version` stays the single
  source of truth.
- Uses a `java.toolchain` block (JDK 25), JUnit 5 Jupiter, and the 2-space Java indentation style captured in
  `.editorconfig`.
- Added `.github/workflows/ci.yml` (`gradlew build` on push/PR), `LICENSE` (MIT), and `docs/index.md` as a
  placeholder for other documentation.
- Found and fixed a bug while verifying `create` end-to-end (scaffold a child, then actually build+test the
  child, not just check that files exist). `CliTest.createScaffoldsRenamedProject` originally
  asserted the *old* placeholder package name (`myproject`) was absent from the scaffolded output. Since that
  test file is itself copied and rewritten by `create`, the literal string `"myproject"` inside its own
  assertion gets swept up in the very rewrite it's checking — silently mutating the assertion into checking
  that the *newly created* package is absent, which is false. Projects scaffolded from this template therefore
  shipped with a failing test out of the box. Fixed here by asserting a structural invariant
  (exactly one package directory under `src/main/java`) instead of a literal name that can't survive being
  copied into its own test subject.
