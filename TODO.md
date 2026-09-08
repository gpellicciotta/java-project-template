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

- [~] T0020 [owner: @codex] Enable source and Javadoc archives in the template and shared multi-project conventions.
- [ ] T0021 Decide whether to configure the javadoc task with HTML5 output and '-Xdoclint:none' / '-quiet': all three real projects independently share this exact configuration, while the template has no javadoc block. **[Decided]**: Yes
- [ ] T0022 Decide whether to replace the blanket .vscode/ ignore in .gitignore with a selective pattern and track shared settings.json: all three real projects commit .vscode/settings.json, but the template's .gitignore blanket-ignores it. **[Decided]**: Yes
- [ ] T0023 Decide whether to add standard Gradle flags (caching, warning.mode, console, logging.level=info) and copyright to gradle.properties: all three real projects set logging.level=info and two configure build caching. **[Decided]**: Yes
- [ ] T0024 Decide whether to update docs/multi-module.md with the 3-tier buildSrc layout (common, library, application) and settings.gradle mapping: both real multi-module projects independently use this structure. **[Decided]**: Yes
- [ ] T0025 Decide whether to expand .gitattributes and .editorconfig to normalize *.cmd/*.sh scripts and declare common binary file types: hinolugi-counters explicitly normalizes scripts and 15 binary formats, whereas the template only covers *.bat. **[Decided]**: Yes
- [ ] T0026 Decide whether to update Scaffold.java to adhere to Dev Guidelines: it currently emits CHANGELOG.md with '[in development]' instead of '-pre', resets version to '0.0.1' instead of '0.1.0-pre', and omits '**Next ID:** 0001' from TODO.md. **[Decided]**: Yes
- [ ] T0027 Decide whether to add .env, `**/*.env`, and bin/ to .gitignore: all three real projects explicitly ignore .env secrets and bin/ compiler output, both currently missing from the template's .gitignore. **[Decided]**: Yes
- [ ] T0028 Decide whether the jar manifest should stamp 'Build-Time' by default: all three real projects stamp Build-Time in their manifests, but doing so compromises Gradle build-cache reproducibility for the jar task. **[Decided]**: Yes
- [ ] T0029 Decide whether to update CLAUDE.md to remove stale references to setup.ps1 and build.gradle's version, documenting scripts/bootstrap-dev-environment.py and deploy-to-production.py instead. **[Decided]**: Yes

---

### Backlog

*(Currently no tasks)*
