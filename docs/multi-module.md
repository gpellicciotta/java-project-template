# Splitting into a multi-project build

This template deliberately stays a single Gradle module (see `CLAUDE.md`'s "Favour simplicity over
ingenuity"). Don't reach for this until there's a real second module — a single module with `src/main` and
`src/test` covers everything a project needs until that point.

## The pattern, once you need it

Share build logic across subprojects with a `buildSrc` convention plugin rather than repeating the same
`plugins {}` / `repositories {}` / `java.toolchain {}` / `test { useJUnitPlatform() }` block in every
subproject's `build.gradle`. Minimal shape (three small files):

`buildSrc/build.gradle` — just enough to compile the convention plugins below:

```groovy
plugins {
  id 'groovy-gradle-plugin'
}

repositories {
  gradlePluginPortal()
}
```

`buildSrc/src/main/groovy/<group>.java-common-conventions.gradle` — everything every subproject needs
(repositories, the JUnit dependencies, the toolchain block, `useJUnitPlatform()`):

```groovy
plugins {
  id 'java'
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation 'org.junit.jupiter:junit-jupiter-api:6.1.3'
  testRuntimeOnly    'org.junit.jupiter:junit-jupiter-engine:6.1.3'
  testRuntimeOnly    'org.junit.platform:junit-platform-launcher:6.1.3'
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(25)
  }
}

test {
  useJUnitPlatform()
}
```

`buildSrc/src/main/groovy/<group>.java-application-conventions.gradle` — a thin subtype for subprojects that
produce a runnable jar:

```groovy
plugins {
  id '<group>.java-common-conventions'
  id 'application'
}
```

(A `<group>.java-library-conventions.gradle` variant applying `java-library` instead of `application` covers
subprojects that are libraries other subprojects depend on.)

Each subproject's own `build.gradle` then shrinks to just `apply plugin: '<group>.java-application-conventions'`
plus whatever's actually specific to that subproject — its own dependencies, its own `mainClass`. The shared
setup lives in one place instead of drifting across N copies.

This is the same shape Gradle's own multi-project sample uses, confirmed against a real-world project
(`hinolugi-counters`) that outgrew a single module.
