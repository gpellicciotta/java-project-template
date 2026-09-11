# DevOps and Operations

Practical guidance on building, testing, developing, and releasing for `java-project-template`.

---

## Prerequisites and Environment

- **JDK**: Java 25 (auto-provisioned by Gradle toolchain if not present locally).
- **Gradle**: Gradle 9.x (managed via the included Gradle wrapper `./gradlew` / `gradlew.bat`).
- **Git**: Git 2.30+ supporting worktrees (`git worktree`).
- **PowerShell / Bash**: PowerShell on Windows, standard POSIX shell on macOS/Linux.
- **GitHub Packages access**: `build.gradle` depends on `com.hinolugi:hinolugi-support`, hosted on the private
  `gpellicciotta/hinolugi-support.java` GitHub Packages repo. Local builds need a GitHub PAT with `read:packages`
  set as `github.token` (and `github.username`) in `{user.home}/.gradle/gradle.properties`, or the
  `GITHUB_USERNAME`/`GITHUB_TOKEN` env vars. CI reads the PAT from the `HINOLUGI_PACKAGES_TOKEN` repo secret.

---

## Development Workflows

### Initial Bootstrap
Clone the repository and run the bootstrap script:
```shell
python scripts/bootstrap-dev-environment.py
```
This initializes a git repo (if one doesn't already exist) and runs `gradlew build`.

### VS Code Workspace Settings

The shared [.vscode/settings.json](../.vscode/settings.json) enables automatic Java build configuration updates.
Keep shared settings portable; configure machine-specific JDK paths and personal preferences in VS Code user settings.
Other files and directories under `.vscode/` remain ignored by default, including in nested modules.

### Local Environment Files and Compiler Output

[.gitignore](../.gitignore) excludes `.env`, `**/*.env`, and `bin/` at the root and within nested modules.
These patterns cover local environment secrets, named files such as `production.env`, and compiler output directories.
Sanitized examples such as `.env.example` remain trackable; exclude real credentials from examples.
Ignore rules do not remove files already tracked by Git.

### Line Endings and Binary Assets

[.gitattributes](../.gitattributes) normalizes text to LF in Git and checks out `.bat` and `.cmd` scripts with CRLF.
Shell scripts (`.sh`) and `gradlew` use LF; `gradlew.bat` uses CRLF.
[.editorconfig](../.editorconfig) applies matching line endings when saving scripts in compatible editors.

Git treats `.png`, `.jpg`, `.jpeg`, `.gif`, `.ico`, `.pdf`, `.docx`, `.jar`, `.war`, `.class`,
`.woff`, `.woff2`, `.ttf`, and `.eot` files as binary, disabling text conversion, text diffs, and text merging.
The general `.jar` rule also protects the Gradle wrapper JAR.

### Deploying a Service
`scripts/deploy-to-production.py` is a scaffolded placeholder per the dev-guidelines scripts-directory
convention. This template ships as a standalone CLI jar with no production service, so the script just
prints guidance; projects derived from this template that deploy a service should replace its body with
their real deployment steps.

### Creating a Release with `create-github-release.py`

The `scripts/create-github-release.py` script automates the complete release process end-to-end:

1. Validates preconditions (clean working tree, branch is `master` or `main`, `gh` CLI installed and authenticated, no existing remote tag or release).
2. Runs pre-flight quality checks (`spotlessCheck`, `test`, `build`).
3. Extracts release notes for the target version from `CHANGELOG.md`.
4. Finalizes the version in `gradle.properties` and `CHANGELOG.md` (removing `-pre` and stamping the release date).
5. Commits the finalized release files and creates the git tag (`v<version>`).
6. Pushes the commit and tag to GitHub and creates the GitHub release.
7. Opens the next patch development version (`-pre`) in `gradle.properties` and `CHANGELOG.md` in a follow-up commit.

```shell
# Preview the release actions without making changes
python scripts/create-github-release.py --dry-run

# Create and publish the release
python scripts/create-github-release.py release

# Create and publish while skipping pre-flight checks
python scripts/create-github-release.py release --skip-checks
```

### Building and Testing

The root [gradle.properties](../gradle.properties) defines shared build defaults:

- `org.gradle.caching=true` reuses eligible task outputs through Gradle's local build cache.
- `org.gradle.warning.mode=all` displays each warning.
- `org.gradle.console=rich` enables colored output and progress indicators.
- `org.gradle.logging.level=info` includes detailed build diagnostics.

For plain automation logs, run `./gradlew build --console=plain` (Windows: `.\gradlew.bat build --console=plain`).
Use `--no-build-cache`, `--warning-mode=summary`, or `--quiet` to override individual defaults for one invocation.
See the [Gradle build environment reference](https://docs.gradle.org/current/userguide/build_environment.html) for property precedence and supported values.

The same properties file stores `author` and a literal `copyright` notice for derived projects and build consumers.
Update both when changing ownership; `copyright` does not interpolate `author`.
The existing CLI copyright and JAR vendor continue to derive from `author`.

```powershell
# Run tests only
.\gradlew.bat test

# Full compile, test, and assembly of application, source, and Javadoc jars
.\gradlew.bat build

# Run application during development
.\gradlew.bat run --args="help"
.\gradlew.bat run --args="greet Gio"
```

The build writes the main jar, `-sources.jar`, and `-javadoc.jar` to `build/libs/`.
Run `sourcesJar` or `javadocJar` to generate either companion archive individually.
Javadoc explicitly uses HTML5 output with `-Xdoclint:none` and `-quiet`.
This disables documentation lint diagnostics and suppresses generation progress messages; Javadoc errors still fail the build.

### JAR Build Timestamp

The application JAR manifest includes `Build-Time` by default, formatted as an ISO-8601 UTC instant ending in `Z`.
The value records when Gradle configures the manifest, rather than when compilation or packaging finishes.
Source and Javadoc JARs do not receive this attribute.

This deliberately favors build traceability over byte-for-byte reproducibility.
Each fresh configuration changes the manifest input, causing `jar` to execute again even when source code is unchanged.
Distributions containing that JAR also change.
Gradle does not cache `Jar` tasks by default; enabling caching explicitly would still encounter changing timestamp inputs.
Compilation and other eligible tasks retain their normal incremental and build-cache behavior.
Derived projects requiring reproducible archives should remove the `Build-Time` attribute or replace it with a fixed release value.
See Gradle's [incremental build](https://docs.gradle.org/current/userguide/incremental_build.html) and
[build cache](https://docs.gradle.org/current/userguide/build_cache.html) documentation.

### Task Coordination Protocol
All task work follows the protocol in [Coordinating Work Guidelines](https://github.com/gpellicciotta/dev-guidelines/blob/main/guidelines/coordinating-work-guidelines.md).
Task weight depends on isolation and tracking needs:
- **Full task (`Tnnnn`)**: needs a branch/worktree, a dedicated plan, or progress tracking.
  1. **Claim**: On `master`, change the line in `TODO.md` from `[ ]` to `[~] [owner: @name]`, commit, push. First
     fast-forward push wins; on rejection, re-fetch and pick a different task.
  2. **Worktree**: `git worktree add ./work/Tnnnn-slug -b task/Tnnnn-slug`.
  3. **Execute**: Work only inside that worktree; maintain `tasks/Tnnnn-slug.md`.
  4. **Finalize**: Pass all tests, update documentation and `CHANGELOG.md`, integrate into mainline in a single
     commit, remove the worktree/branch, and clear the entry from `TODO.md`.
- **Adhoc task (`Annnn`)**: completable by one actor in one sitting with low collision risk — no branch or
  worktree, edited directly in the primary checkout, same claim-then-integrate commit flow as above.

---

## Scaffolding a New Project

Generated projects start at `0.1.0-pre` in `gradle.properties`, with a matching `## v0.1.0-pre` changelog heading.
Their fresh `TODO.md` starts with `**Next ID:** 0001` and empty Next Milestone and Backlog sections.

To create a new project from this template:
```powershell
# Using the CLI
java -jar build/libs/template-project-<version>.jar create my-new-tool -o C:\Dev-Projects

# Or via Gradle run
.\gradlew.bat run --args="create my-new-tool -o C:\Dev-Projects"
```

---

## Release Process

Ongoing work accumulates under the top `CHANGELOG.md` heading while it carries a `-pre` SemVer suffix (e.g.
`## v1.1.1-pre`), which must always match `version` in `gradle.properties` (the single source of truth) exactly,
`-pre` included.

### Automated Release with `create-github-release.py`

Use `scripts/create-github-release.py` to automate the complete release flow:

```shell
# Validate and preview release actions
python scripts/create-github-release.py release --dry-run

# Run automated release
python scripts/create-github-release.py release
```

The script performs the following operations:
- Verifies a clean working tree and checks that the `gh` CLI is available.
- Extracts release notes from `CHANGELOG.md` for the current version.
- Finalizes the `-pre` heading in `CHANGELOG.md` and strips `-pre` in `gradle.properties`.
- Commits the finalized files and tags the release.
- Pushes the mainline branch and tag to `origin`.
- Creates a GitHub release using `gh release create` with the extracted release notes.
- Opens the next development version in `CHANGELOG.md` and `gradle.properties`, then commits and pushes.

### Gradle `doFullRelease` Pipeline

Alternatively, run the Gradle-native release pipeline:
```powershell
.\gradlew.bat doFullRelease
```
This runs `build` (compile, Spotless, tests), then in order:
- `checkChangelogUpToDate` - fails if the tree is dirty, `version` has no `-pre` suffix, or `CHANGELOG.md`
  has no matching un-released heading.
- `publishReleaseArtifacts` - runs `publishMavenPublicationToGithubPackagesRepository` if this project has
  opted into `maven-publish` per `docs/library-publishing.md`; otherwise a no-op (this template ships as an
  application, not a library, so nothing is published by default).
- `markChangelogReleased` - replaces the CHANGELOG's `-pre` suffix with `[released: <date>]` and commits it.
- `tagAndPushRelease` - tags the release commit `v<version>` and pushes the branch and tag to `origin`.
- `createGithubRelease` - runs `gh release create` for the new tag, using the CHANGELOG section as notes
  (requires the [GitHub CLI](https://cli.github.com/) installed and authenticated via `gh auth login`).
- `openNextPreRelease` - bumps `gradle.properties` to the next patch `-pre` version, opens a matching
  `CHANGELOG.md` heading, and commits and pushes — so the tree is ready for further development with no
  manual follow-up step.

Any individual step can also be run on its own (e.g. `.\gradlew.bat checkChangelogUpToDate`) to diagnose or
resume a failed release without repeating the steps already done.

---

## Continuous Integration

The GitHub Actions workflow in `.github/workflows/ci.yml` triggers on push and pull requests, executing:
- Java JDK 25 setup.
- `./gradlew build` (compilation, unit testing, packaging, and a Spotless formatting check via `check`).
