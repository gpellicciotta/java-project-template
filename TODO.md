# TODO

An overview of all tasks and their planning.

> Tasks are listed by milestone.
> See [coordinating work guidelines](https://github.com/gpellicciotta/dev-guidelines/blob/main/guidelines/coordinating-work-guidelines.md) for the full coordination protocol.
>
> Status: `[ ]` available · `[~]` active · `[!]` blocked · `[?]` needs-review
> Owner: `[owner: @name]` shown only when active/blocked/needs-review.
> Dependencies: `[needs: Tnnnn]` shown only when unresolved.

**Next ID:** 0030

---

## Next Milestone

- [~] T0025 [owner: @codex] Normalize Windows and Unix scripts and declare common binary file types in Git attributes and EditorConfig.
- [ ] T0026 Decide whether to update Scaffold.java to adhere to Dev Guidelines: it currently emits CHANGELOG.md with '[in development]' instead of '-pre', resets version to '0.0.1' instead of '0.1.0-pre', and omits '**Next ID:** 0001' from TODO.md. **[Decided]**: Yes
- [ ] T0027 Decide whether to add .env, `**/*.env`, and bin/ to .gitignore: all three real projects explicitly ignore .env secrets and bin/ compiler output, both currently missing from the template's .gitignore. **[Decided]**: Yes
- [ ] T0028 Decide whether the jar manifest should stamp 'Build-Time' by default: all three real projects stamp Build-Time in their manifests, but doing so compromises Gradle build-cache reproducibility for the jar task. **[Decided]**: Yes
- [ ] T0029 Decide whether to update CLAUDE.md to remove stale references to setup.ps1 and build.gradle's version, documenting scripts/bootstrap-dev-environment.py and deploy-to-production.py instead. **[Decided]**: Yes

---

### Backlog

*(Currently no tasks)*
