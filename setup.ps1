# Open PowerShell in project root

# Start the project under version control if it isn't already
if (-not (Test-Path .git)) {
    git init
}

# Build, run tests, and produce the jar. The Gradle wrapper pins the exact Gradle version, and the toolchain
# block in build.gradle pins the exact JDK version (auto-provisioned by Gradle if not already installed) - no
# separate environment bootstrap step is needed the way Python's venv requires one.
.\gradlew.bat build
