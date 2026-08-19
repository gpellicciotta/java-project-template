# Release Notes

All notes will be in reverse chronological order.

## 2026-08-19 v0.1.0
- First working version of the template: a minimal Gradle/Java project (`src/main/java`+`src/test/java`
  layout, JUnit 5, the Gradle wrapper with a pinned toolchain JDK) plus the `RELEASES.md`/`TODO.md`/`CLAUDE.md`
  docs/versioning conventions shared with its Python counterpart and every other project scaffolded from
  either template.
- `Cli.java` gained `help`/`version`/`greet <name>`/`create <project-name> [-o <output-dir>]` subcommands,
  mirroring the Python template's CLI shape. `create` scaffolds a new project as a renamed copy of this
  template (`Scaffold.java`), the Java equivalent of the Python template's `scaffold.py`.
- The version is stamped into the jar manifest and read back at runtime via
  `Package.getImplementationVersion()` rather than hardcoded, mirroring the Python template's
  `importlib.metadata.version(...)` approach — `build.gradle`'s `version` stays the single source of truth.
- Adopted `hinolugi-counters`' established best practices: a `java.toolchain` block (JDK 25) instead of
  `sourceCompatibility`/`targetCompatibility`, JUnit 5 Jupiter (matching `hinolugi-support`'s own test setup),
  and its actual 2-space Java indentation style (captured in `.editorconfig`) rather than the 4-space
  convention used elsewhere.
- Added `.github/workflows/ci.yml` (`gradlew build` on push/PR), `LICENSE` (MIT), and `docs/README.md` as a
  placeholder for other documentation.
- Found and fixed a bug while verifying `create` end-to-end (scaffold a child, then actually build+test the
  child, not just check that files exist) — the Python template has the same live bug, confirmed by running
  its own test suite against a project scaffolded from it. `CliTest.createScaffoldsRenamedProject` originally
  asserted the *old* placeholder package name (`myproject`) was absent from the scaffolded output. Since that
  test file is itself copied and rewritten by `create`, the literal string `"myproject"` inside its own
  assertion gets swept up in the very rewrite it's checking — silently mutating the assertion into checking
  that the *newly created* package is absent, which is false. Every project scaffolded from either template
  therefore shipped with a failing test out of the box. Fixed here by asserting a structural invariant
  (exactly one package directory under `src/main/java`) instead of a literal name that can't survive being
  copied into its own test subject.
