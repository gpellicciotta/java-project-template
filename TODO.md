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

- [ ] A0019 Make doFullRelease atomically open the next patch `-pre` CHANGELOG heading and bump `gradle.properties` after tagging, instead of leaving that as a manual follow-up step.

---

### Backlog

- [?] T0020 [owner: @gio] Decide whether build.gradle and docs/multi-module.md should configure 'withJavadocJar()' and 'withSourcesJar()' in the java block: all three real projects configure both across all subprojects, but the template does not.
- [?] T0021 [owner: @gio] Decide whether to configure the javadoc task with HTML5 output and '-Xdoclint:none' / '-quiet': all three real projects independently share this exact configuration, while the template has no javadoc block.
- [?] T0022 [owner: @gio] Decide whether to replace the blanket .vscode/ ignore in .gitignore with a selective pattern and track shared settings.json: all three real projects commit .vscode/settings.json, but the template's .gitignore blanket-ignores it.
- [?] T0023 [owner: @gio] Decide whether to add standard Gradle flags (caching, warning.mode, console, logging.level=info) and copyright to gradle.properties: all three real projects set logging.level=info and two configure build caching.
- [?] T0024 [owner: @gio] Decide whether to update docs/multi-module.md with the 3-tier buildSrc layout (common, library, application) and settings.gradle mapping: both real multi-module projects independently use this structure.
- [?] T0025 [owner: @gio] Decide whether to expand .gitattributes and .editorconfig to normalize *.cmd/*.sh scripts and declare common binary file types: hinolugi-counters explicitly normalizes scripts and 15 binary formats, whereas the template only covers *.bat.
- [?] T0026 [owner: @gio] Decide whether to update Scaffold.java to adhere to Dev Guidelines: it currently emits CHANGELOG.md with '[in development]' instead of '-pre', resets version to '0.0.1' instead of '0.1.0-pre', and omits '**Next ID:** 0001' from TODO.md.
- [?] T0027 [owner: @gio] Decide whether to add .env, **/*.env, and bin/ to .gitignore: all three real projects explicitly ignore .env secrets and bin/ compiler output, both currently missing from the template's .gitignore.
- [?] T0028 [owner: @gio] Decide whether the jar manifest should stamp 'Build-Time' by default: all three real projects stamp Build-Time in their manifests, but doing so compromises Gradle build-cache reproducibility for the jar task.
- [?] T0029 [owner: @gio] Decide whether to update CLAUDE.md to remove stale references to setup.ps1 and build.gradle's version, documenting scripts/bootstrap-dev-environment.py and deploy-to-production.py instead.




















