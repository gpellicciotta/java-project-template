# T0001: Fix gradlew missing its executable bit

- **Status:** Completed   (note: mirrors TODO.md; TODO.md is authoritative)
- **Owner:** claude
- **Started:** 2026-08-24
- **Branch:** main (no dedicated branch — see Progress Log)
- **Worktree:** none

## Goal

`gradlew` is committed with file mode `100644` (non-executable) instead of `100755`. On a Linux CI runner
(`ubuntu-latest`, as used in `.github/workflows/ci.yml`), `./gradlew build` fails immediately with
`Permission denied` (exit 126) — the build step never actually runs.

## Scope

Fix the executable bit on `gradlew` in this repo (`git update-index --chmod=+x gradlew`, then commit), and check
whether `Scaffold.java`'s `create` command copies the file in a way that preserves (or needs to explicitly set)
the executable bit for newly scaffolded projects too.

## Out of Scope

Nothing beyond the executable-bit fix itself.

## Dependencies

None.

## Approach

Discovered while aligning `hinolugi-support.java` with this template: its own CI (added as part of that
alignment) failed with this exact error, root-caused, and fixed there
(`git update-index --chmod=+x gradlew`). This template has the same underlying `gradlew` file (identical git
blob hash), so it very likely has the identical bug, but its own CI workflow hasn't been observed to run/fail
yet to confirm.

## Implementation Checklist

- [x] `git update-index --chmod=+x gradlew` — file mode now `100755` (was `100644`).
- [x] Checked `Scaffold.java`'s `create` command: `copyTree` uses
  `Files.copy(path, target, StandardCopyOption.COPY_ATTRIBUTES)`, which preserves POSIX permissions
  (including the executable bit) on copy on POSIX filesystems. No code change needed — the bug was purely
  the git-tracked mode of the committed `gradlew` file itself, which every checkout (and every `create`
  invocation on a POSIX system) inherits from.

## Test Strategy

No unit test applicable (this is a git-metadata fix, not application logic). Verification is a real
`ubuntu-latest` CI run actually succeeding at `./gradlew build` post-push.

## Completion Criteria

- `git ls-files -s gradlew` reports mode `100755`.
- Next CI run on `main` does not fail with `Permission denied` on the `./gradlew build` step.

## Progress Log

- 2026-08-24: Fixed directly on `main` (no worktree/branch — single-file git-metadata change, same
  low-risk profile as an adhoc task, though filed as `Tnnnn` since it predates the `Annnn` convention and
  already had a task file).

## Validation Record

Solo/agent-only; no separate reviewer. `git ls-files -s gradlew` confirms mode `100755` locally. The
actual CI-runner behavior (does `./gradlew build` now execute instead of hitting `Permission denied`)
is unverified by this session — confirm on the next push's Actions run.

## Completion Record

- **Completed:** 2026-08-24
- **Outcome:** `gradlew`'s git file mode fixed to `100755`. No `Scaffold.java` change needed.
