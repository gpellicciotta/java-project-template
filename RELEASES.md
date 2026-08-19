# Release Notes

All notes will be in reverse chronological order.

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
