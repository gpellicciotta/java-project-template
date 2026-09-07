# DevOps and Operations

Practical guidance on building, testing, developing, and releasing for `java-project-template`.

---

## 1. Prerequisites and Environment

- **JDK**: Java 25 (auto-provisioned by Gradle toolchain if not present locally).
- **Gradle**: Gradle 9.x (managed via the included Gradle wrapper `./gradlew` / `gradlew.bat`).
- **Git**: Git 2.30+ supporting worktrees (`git worktree`).
- **PowerShell / Bash**: PowerShell on Windows, standard POSIX shell on macOS/Linux.
- **GitHub Packages access**: `build.gradle` depends on `com.hinolugi:hinolugi-support`, hosted on the private
  `gpellicciotta/hinolugi-support.java` GitHub Packages repo. Local builds need a GitHub PAT with `read:packages`
  set as `github.token` (and `github.username`) in `{user.home}/.gradle/gradle.properties`, or the
  `GITHUB_USERNAME`/`GITHUB_TOKEN` env vars. CI reads the PAT from the `HINOLUGI_PACKAGES_TOKEN` repo secret.

---

## 2. Development Workflows

### 2.1. Initial Bootstrap
Clone the repository and run the bootstrap script:
```shell
python scripts/bootstrap-dev-environment.py
```
This initializes a git repo (if one doesn't already exist) and runs `gradlew build`.

### 2.2. Deploying a Service
`scripts/deploy-to-production.py` is a scaffolded placeholder per the dev-guidelines scripts-directory
convention. This template ships as a standalone CLI jar with no production service, so the script just
prints guidance; projects derived from this template that deploy a service should replace its body with
their real deployment steps.

### 2.3. Building and Testing
```powershell
# Run tests only
.\gradlew.bat test

# Full compile, test, and jar assembly
.\gradlew.bat build

# Run application during development
.\gradlew.bat run --args="help"
.\gradlew.bat run --args="greet Gio"
```

### 2.4. Task Coordination Protocol
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

## 3. Scaffolding a New Project

To create a new project from this template:
```powershell
# Using the CLI
java -jar build/libs/template-project-<version>.jar create my-new-tool -o C:\Dev-Projects

# Or via Gradle run
.\gradlew.bat run --args="create my-new-tool -o C:\Dev-Projects"
```

---

## 4. Release Process

1. Choose the next semantic version and update `version` in `gradle.properties` (the single source of truth for
   the jar manifest and distribution), keeping its `-pre` suffix.
2. Add or update the corresponding `-pre` entry at the top of `CHANGELOG.md`.
3. Commit and push that work to `master` — `doFullRelease` (below) refuses to run against an unclean tree.
4. Run the full release pipeline:
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

## 5. Continuous Integration

The GitHub Actions workflow in `.github/workflows/ci.yml` triggers on push and pull requests, executing:
- Java JDK 25 setup.
- `./gradlew build` (compilation, unit testing, packaging, and a Spotless formatting check via `check`).

