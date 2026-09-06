# TODO

An overview of all tasks and their planning.

> Tasks are listed by milestone.
> See [coordinating work guidelines](https://github.com/gpellicciotta/dev-guidelines/blob/main/guidelines/coordinating-work-guidelines.md) for the full coordination protocol.
>
> Status: `[ ]` available · `[~]` active · `[!]` blocked · `[?]` needs-review
> Owner: `[owner: @name]` shown only when active/blocked/needs-review.
> Dependencies: `[needs: Tnnnn]` shown only when unresolved.

**Next ID:** 0017

---

## Next Milestone

- [?] A0013 [owner: @claude] Upgrade to using the latest released hinolugi-support.java as we want derived projects also to do that from the beginning.
  **[Feedback Needed]** Drafted `implementation 'com.hinolugi:hinolugi-support:5.0.0'` plus the GitHub Packages
  repo block (mirroring hinolugi-auth), CI env wiring (mirroring hinolugi-counters), and README/CHANGELOG notes;
  build passed locally. Reverted before committing: `gpellicciotta/hinolugi-support.java` is a **private** repo,
  so baking this into the **public** java-project-template means only @gio (with repo access) can ever build a
  project scaffolded from it — every other consumer's `gradlew build` fails on package auth, even with their own
  valid PAT. Also: this repo has no `HINOLUGI_PACKAGES_TOKEN` secret yet, so CI would fail immediately too.
  Please decide: (a) proceed anyway since derived projects are expected to stay in the private hinolugi family,
  and add the `HINOLUGI_PACKAGES_TOKEN` CI secret, or (b) make hinolugi-support.java public first, or (c) follow
  A0012 instead — document the GitHub Packages pattern in docs/ as optional rather than a default dependency.
- [~] T0007 [owner: @claude] The jar manifest should append a git-commit-hash build-metadata suffix to Implementation-Version by default, as the versioning guideline requires: only hinolugi-support.java currently computes 'git rev-parse --short HEAD' and appends '+<hash>'; neither the template nor hinolugi-auth's manifests do this.
- [ ] T0008 The template should ship a minimal reference logging implementation (formatted log lines, --debug, --log-file) instead of a bare Cli.java: today it only supports --verbose/help/version, while both real projects independently built their own logging wrapper (hinolugi-auth's Logs.java, hinolugi-support.java's full logging SPI) to satisfy the same guideline.
- [ ] A0009 The template's default .gitignore should ignore all *.log files and any files in log/.
  It should switch from blanket-ignoring '.idea/' to the selective pattern both real projects use: checking in shared IntelliJ config while excluding only volatile files like workspace.xml, dataSources.local.xml, shelf/, and sonarlint caches.
- [ ] A0011 Extract group/version/author/vendor metadata out of build.gradle into a dedicated gradle.properties file: both hinolugi-auth and hinolugi-support.java independently made this move, while the template still inlines 'group', 'version', and 'ext.vendor' directly in build.gradle.
- [ ] A0012 Document (not bake in) an optional library-publishing pattern in docs/, covering the maven-publish plugin, a GitHub Packages repository block, and withJavadocJar()/withSourcesJar(): hinolugi-support.java needs all of this to be consumed by hinolugi-auth, but it's only relevant to template-derived projects that publish a reusable library.
- [ ] A0014 [needs: A0013 A0012 A0011 A0009 T0008 T0007] Make a new v1.1.0 release
- [ ] T0015 [needs: A0014] Review the Java projects hinolugi-support.java, hinolugi-counters and hinolugi-auth and document structural differences from this project: look at it as if these projects should have been started from this project, yet keeping in mind that this is a generic template project: we don't want to include things that are only relevant for 1 particular project. For each major topic, make a separate TODO in the backlog and mark it for review by @gio.
- [ ] T0016 Add a `gradlew doFullRelease` that make sure the CHANGELOG is up-to-date and committed (and the latest version not released yet), then run `gradle publishMavenPublicationToGithubPackagesRepository`, mark the CHANGELOG entry with [released: {{date}}], git tag and git push origin, then gh release create. So basically, based on committed work, do everything to actually make the packages available for use.

---

### Backlog
















