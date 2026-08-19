# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

<Fill in: what this project does, who/what it's for (personal tool, library, service, ...), and any hard
invariants that must not be silently changed — e.g. "read-only, never sends/writes", "no network calls",
"single-user, no auth". State them as deliberate design decisions, not just current behavior, so a future
change doesn't casually cross them.>

## Commands

Windows shell; `gradlew.bat` (POSIX shells use `./gradlew`). The Gradle wrapper pins the exact Gradle version;
`build.gradle`'s `java.toolchain` pins the exact JDK version (auto-provisioned by Gradle if not already
installed) — there is no separate environment bootstrap step to run first, unlike a Python venv.

- One-shot bootstrap: `. .\setup.ps1` (git init if needed, then `gradlew build`)
- Build + test + jar: `gradlew build`
- Run the CLI: `gradlew run --args="<command> [args]"` — or `java -jar build/libs/<name>-<version>.jar <command>
  [args]` after `gradlew build`
- Run the test suite only: `gradlew test`
- <Add any other project-specific commands here.>

## Architecture

<Describe the packages and what each one owns — one paragraph per package is usually enough. Name it, state
its one job, and flag anything a future change needs to respect (e.g. "the only place that parses X", "must
stay pure/dependency-free so it's testable without live credentials").>

## Development Guidelines

These apply to every project scaffolded from this template, not just this one:

- Use semver (`MAJOR.MINOR.PATCH`). Projects scaffolded from this template start pre-1.0 (`0.x.y` — see
  `Scaffold.java`'s version reset), so breaking changes are still expected early on but must be called out
  explicitly in `RELEASES.md` rather than reading as routine. Once a project reaches `1.0.0`, a breaking
  change requires a major version bump instead.
- Favour simplicity over ingenuity. Keep things as simple as possible for what's actually needed today — don't
  design for hypothetical future requirements. Stay a single Gradle project as long as that's true; only split
  into a multi-project build (with a `buildSrc` convention plugin sharing the toolchain/test setup across
  subprojects — see `hinolugi-counters/buildSrc` for a worked example) once there's a real second module.
- Minimize third-party dependencies. The JDK and JUnit are enough to start; add a dependency only when it
  earns its ongoing maintenance cost.
- Make it easy to get started: a clone + `. .\setup.ps1` (or `gradlew build`) should be enough to get a
  passing build with tests run — no undocumented setup steps.
- Use a `java.toolchain` block (not `sourceCompatibility`/`targetCompatibility`) to pin the JDK version — it
  decouples the JDK compiling the code from whatever JDK happens to run Gradle, and compiles with `--release`
  semantics so use of APIs newer than the target is caught at build time, not at runtime on an older JDK.

## Conventions

- `RELEASES.md` (top-level) tracks version history: bump `build.gradle`'s `version` for every user-facing
  change and add a matching dated entry to `RELEASES.md` with the same version number. Pre-1.0 (`0.x.y`)
  projects should call out breaking changes explicitly in the entry rather than letting them read as a routine
  addition; at `1.0.0`+, a breaking change requires a major version bump instead.
- `TODO.md` (top-level) is the prioritized backlog. When a TODO item is implemented, remove it and add the
  corresponding `RELEASES.md` entry instead of leaving both.
- `RELEASES.md` and `TODO.md` live at the repo root, not under `docs/`, for visibility. Other documentation
  (design notes, detailed plans, investigation write-ups) belongs under `docs/` instead.
- Keep `README.md`'s setup/usage/layout sections in sync with the code as it evolves — treat drift there as a
  bug, not a documentation nice-to-have.
- Style guide: 2-space indentation (never tabs), UTF-8, LF line endings everywhere — matching the style
  actually used across `hinolugi-counters`' Java code, not the 4-space convention common elsewhere. Enforced by
  `.editorconfig`; don't let it drift from these numbers.
- The version is stamped into the jar manifest (`build.gradle`'s `jar { manifest { ... } }`) and read back at
  runtime via `Package.getImplementationVersion()` (`Cli.java`'s `version()`, with a `"0.0.0+unknown"` fallback
  for when running from compiled classes rather than a jar, e.g. `gradlew run` or the test suite) rather than
  hardcoded, so `build.gradle`'s `version` stays the single source of truth — mirrors
  the Python template's `importlib.metadata.version(...)` approach; deliberately not a second
  hand-maintained constant, which is exactly the kind of thing that drifts.
- <Add project-specific invariants/conventions here as they emerge — things a future change must not casually
  break.>
