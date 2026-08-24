# T0002: Upgrade deprecated GitHub Actions versions in ci.yml

- **Status:** Completed   (note: mirrors TODO.md; TODO.md is authoritative)
- **Owner:** claude
- **Started:** 2026-08-24
- **Branch:** main (no dedicated branch — see Progress Log)
- **Worktree:** none

## Goal

`.github/workflows/ci.yml` uses `actions/checkout@v4` and `actions/setup-java@v4`, both of which now emit
deprecation warnings on GitHub-hosted runners: Node.js 20 (the runtime these action versions target) is
deprecated in favor of Node 24, and `actions/setup-java@v4` itself is deprecated in favor of `@v5`. Builds still
succeed today, but these are heading toward being forced onto an unsupported runtime.

## Scope

Bump `actions/checkout` and `actions/setup-java` to their latest major versions in `ci.yml`, verify the workflow
still succeeds, and check `actions/setup-java@v5`'s changelog for any input/behavior changes that affect this
template's usage (distribution/java-version inputs).

## Out of Scope

Any other CI changes.

## Dependencies

None.

## Approach

Observed while aligning `hinolugi-support.java` with this template: its (new, copied-from-this-template) CI run
succeeded but printed these same two deprecation annotations. This template's `ci.yml` has the identical action
versions, so it's very likely affected too, though not independently confirmed by an actual run here.

## Implementation Checklist

- [x] Checked actual current latest major releases via the GitHub API (not assumed) — `actions/checkout`
  is at `v7.0.1` (this task's original note guessing `v5` was stale), `actions/setup-java` is at `v6.0.0`
  (released 2026-08-24, the same day as this fix — very fresh).
- [x] Reviewed both changelogs for breaking changes relevant to this workflow's usage:
  - `checkout@v7`'s only behavioral change (refusing fork-PR checkout under `pull_request_target` /
    `workflow_run`) doesn't apply — this workflow only triggers on `push`/`pull_request`.
  - `setup-java@v6`'s changes (Zulu → Azul metadata API, ESM migration, removal of legacy Adopt
    distribution) don't affect this workflow, which uses `distribution: 'temurin'` with a plain
    `java-version` input; the ESM migration is explicitly documented upstream as not user-facing.
- [x] Bumped `.github/workflows/ci.yml`: `actions/checkout@v4` → `@v7`, `actions/setup-java@v4` → `@v6`.

## Test Strategy

No unit test applicable (CI config only). Verification is a real `ubuntu-latest` run succeeding without
the prior deprecation annotations.

## Completion Criteria

- `ci.yml` no longer pins the deprecated `@v4` majors.
- Next CI run on `main` succeeds without the Node 20 / setup-java deprecation warnings.

## Progress Log

- 2026-08-24: Looked up actual current release data (GitHub API, not memory/guesswork, since versions
  move fast and the task's own note already understated `checkout`'s current major). Bumped both actions
  directly on `main`.

## Validation Record

Solo/agent-only; no separate reviewer. Version numbers and changelog compatibility confirmed via the
GitHub API and release notes (see Implementation Checklist). `setup-java@v6.0.0` is a same-day release at
the time of this fix — if CI misbehaves after this push, the fallback is pinning back to `@v5` rather than
assuming the workflow itself is at fault. Actual green CI run is unverified by this session — confirm on
the next push's Actions run.

## Completion Record

- **Completed:** 2026-08-24
- **Outcome:** `ci.yml` bumped to `actions/checkout@v7` and `actions/setup-java@v6`.
