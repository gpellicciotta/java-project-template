# TODO

An overview of all tasks and their planning.

> Tasks are listed by milestone.
> See [coordinating work guidelines](https://github.com/gpellicciotta/dev-guidelines/blob/main/guidelines/coordinating-work-guidelines.md) for the full coordination protocol.
>
> Status: `[ ]` available · `[~]` active · `[!]` blocked · `[?]` needs-review
> Owner: `[owner: @name]` shown only when active/blocked/needs-review.
> Dependencies: `[needs: Tnnnn]` shown only when unresolved.

**Next ID:** 0019

---

## Next Milestone

- [?] A0013 [owner: @claude] Upgrade to using the latest released hinolugi-support.java as we want derived projects also to do that from the beginning.
  **[Decided]** Option (a): proceed with the private dependency, derived projects stay in the hinolugi family.
  Implemented `com.hinolugi:hinolugi-support:5.1.0` plus the GitHub Packages repo block in `build.gradle`, CI env
  wiring in `.github/workflows/ci.yml`, and README/devops.md/CHANGELOG notes; build passes locally.
  **[Feedback Needed]** Add the `HINOLUGI_PACKAGES_TOKEN` repo secret (Settings > Secrets and variables > Actions,
  a PAT with `read:packages`) — I can't set repo secrets myself. CI stays red until it's added.
- [ ] A0014 [needs: A0013] Make a new v1.1.0 release (so renaming the ongoing v1.0.1-pre)
- [ ] T0015 [needs: A0014] Review the Java projects hinolugi-support.java, hinolugi-counters and hinolugi-auth and document structural differences from this project: look at it as if these projects should have been started from this project, yet keeping in mind that this is a generic template project: we don't want to include things that are only relevant for 1 particular project. For each major topic, make a separate TODO in the backlog and mark it for review by @gio.

---

### Backlog

*(Currently no tasks)*

















