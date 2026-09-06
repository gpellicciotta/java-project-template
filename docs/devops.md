# DevOps and Operations

Practical guidance on building, testing, developing, and releasing for `java-project-template`.

---

## 1. Prerequisites and Environment

- **JDK**: Java 25 (auto-provisioned by Gradle toolchain if not present locally).
- **Gradle**: Gradle 9.x (managed via the included Gradle wrapper `./gradlew` / `gradlew.bat`).
- **Git**: Git 2.30+ supporting worktrees (`git worktree`).
- **PowerShell / Bash**: PowerShell on Windows, standard POSIX shell on macOS/Linux.

---

## 2. Development Workflows

### 2.1. Initial Bootstrap
Clone the repository and run the setup script:
```powershell
# Windows
. .\setup.ps1

# POSIX (Linux/macOS)
./gradlew build
```

### 2.2. Building and Testing
```powershell
# Run tests only
.\gradlew.bat test

# Full compile, test, and jar assembly
.\gradlew.bat build

# Run application during development
.\gradlew.bat run --args="help"
.\gradlew.bat run --args="greet Gio"
```

### 2.3. Task Coordination Protocol
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

1. Choose the next semantic version and update `version` in `build.gradle` (the single source of truth for the jar manifest and distribution).
2. Add a corresponding entry at the top of `CHANGELOG.md`.
3. Perform a clean verification build:
   ```powershell
   .\gradlew.bat clean build
   ```
4. Commit changes, tag the release, and push:
   ```bash
   git add build.gradle CHANGELOG.md
   git commit -m "Release v0.4.0"
   git tag -a v0.4.0 -m "Release v0.4.0"
   git push origin master --follow-tags
   ```
5. Create a GitHub Release referencing the tag and attach the packaged jar file from `build/libs/`.

---

## 5. Continuous Integration

The GitHub Actions workflow in `.github/workflows/ci.yml` triggers on push and pull requests, executing:
- Java JDK 25 setup.
- `./gradlew build` (compilation, unit testing, packaging, and a Spotless formatting check via `check`).

