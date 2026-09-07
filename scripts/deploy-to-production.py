#!/usr/bin/env python3
"""
Scaffolded example deploy script for the scripts-directory convention (see dev-guidelines).

This template ships as a standalone CLI jar, not a deployed service, so there is nothing to
deploy here by default. Projects derived from this template that do deploy a service should
replace the body of main() below with their real steps (build/publish the artifact, push a
container image, trigger the target environment, run smoke checks, ...), keeping this file
name so the convention stays discoverable.
"""

import sys


def main() -> int:
    print("deploy-to-production.py is a template placeholder: this project has no production service to deploy.")
    print("Replace this script's body with your project's real deployment steps.")
    return 1


if __name__ == "__main__":
    sys.exit(main())
