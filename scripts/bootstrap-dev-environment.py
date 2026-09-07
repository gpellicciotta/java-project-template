#!/usr/bin/env python3
"""One-shot dev environment bootstrap: init git if needed, then run a full Gradle build."""

import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent


def main() -> int:
    if not (ROOT / ".git").is_dir():
        subprocess.run(["git", "init"], cwd=ROOT, check=True)

    gradlew = ROOT / ("gradlew.bat" if sys.platform == "win32" else "gradlew")
    subprocess.run([str(gradlew), "build"], cwd=ROOT, check=True)
    return 0


if __name__ == "__main__":
    sys.exit(main())
