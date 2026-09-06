# TODO

An overview of all tasks and their planning.

> Tasks are listed by milestone.
> See [coordinating work guidelines](https://github.com/gpellicciotta/dev-guidelines/blob/main/guidelines/coordinating-work-guidelines.md) for the full coordination protocol.
>
> Status: `[ ]` available · `[~]` active · `[!]` blocked · `[?]` needs-review
> Owner: `[owner: @name]` shown only when active/blocked/needs-review.
> Dependencies: `[needs: Tnnnn]` shown only when unresolved.

**Next ID:** 0013

---

## Next Milestone

*(Currently no tasks)*

---

### Backlog

- [?] T0005 [owner: @gio] Decide whether to add a Spotless (or equivalent) formatting/lint gate to build.gradle by default: only hinolugi-support.java wires up 'com.diffplug.spotless' with 'check' depending on 'spotlessCheck'; neither the template nor hinolugi-auth run any formatting or static analysis, despite the guideline requiring it in CI.

- [?] T0006 [owner: @gio] Decide whether to add 'testLogging.showStandardStreams = true' to the template's default test block: both real projects already set it, matching the Java guideline, while the template's test block only calls 'useJUnitPlatform()'.

- [?] T0007 [owner: @gio] Decide whether the jar manifest should append a git-commit-hash build-metadata suffix to Implementation-Version by default, as the versioning guideline requires: only hinolugi-support.java computes 'git rev-parse --short HEAD' and appends '+<hash>'; neither the template nor hinolugi-auth's manifests do this.

- [?] T0008 [owner: @gio] Decide whether the template should ship a minimal reference logging implementation (formatted log lines, --debug, --log-file) instead of a bare Cli.java: today it only supports --verbose/help/version, while both real projects independently built their own logging wrapper (hinolugi-auth's Logs.java, hinolugi-support.java's full logging SPI) to satisfy the same guideline.

- [?] T0009 [owner: @gio] Decide whether to add 'pickup-work-loop*.log' (and similar orchestrator log patterns) to the template's default .gitignore: both hinolugi-auth and hinolugi-support.java gitignore these files, but the template documents the same pickup-work-loop.py pattern without excluding its logs.

- [?] T0010 [owner: @gio] Decide whether the template's .gitignore should switch from blanket-ignoring '.idea/' to the selective pattern both real projects use: checking in shared IntelliJ config while excluding only volatile files like workspace.xml, dataSources.local.xml, shelf/, and sonarlint caches.

- [?] T0011 [owner: @gio] Decide whether to extract group/version/author/vendor metadata out of build.gradle into a dedicated gradle.properties file: both hinolugi-auth and hinolugi-support.java independently made this move, while the template still inlines 'group', 'version', and 'ext.vendor' directly in build.gradle.

- [?] T0012 [owner: @gio] Decide whether to document (not bake in) an optional library-publishing pattern in docs/, covering the maven-publish plugin, a GitHub Packages repository block, and withJavadocJar()/withSourcesJar(): hinolugi-support.java needs all of this to be consumed by hinolugi-auth, but it's only relevant to template-derived projects that publish a reusable library.
