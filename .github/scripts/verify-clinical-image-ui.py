#!/usr/bin/env python3
"""Enforce the runtime presentation policy for audited clinical images."""

from __future__ import annotations

import pathlib
import sys

FILES = [
    "app/src/main/java/com/yomismtz/expedientedeldentista/ui/ClinicalPhotoAtlasV46.kt",
    "app/src/main/java/com/yomismtz/expedientedeldentista/ui/PathologicalVisualV43.kt",
    "app/src/main/java/com/yomismtz/expedientedeldentista/ui/HeadNeckVisualV44.kt",
    "app/src/main/java/com/yomismtz/expedientedeldentista/ui/DentalScreensA.kt",
    "app/src/main/java/com/yomismtz/expedientedeldentista/ui/PhysicalInspectionV44.kt",
    "app/src/main/java/com/yomismtz/expedientedeldentista/ui/OcclusionPhotoAtlasV42.kt",
]

FORBIDDEN = {
    "AsyncImage(": "remote/runtime image loading",
    "ContentScale.Crop": "cropping clinical media",
    "ContentScale.FillBounds": "stretching clinical media",
}

def main() -> int:
    failures: list[str] = []
    for raw in FILES:
        path = pathlib.Path(raw)
        if not path.is_file():
            failures.append(f"{raw}: file missing")
            continue
        text = path.read_text(encoding="utf-8")
        for token, reason in FORBIDDEN.items():
            if token in text:
                failures.append(f"{raw}: forbidden {reason} via {token}")

    if failures:
        print("Clinical image UI policy failed:", file=sys.stderr)
        for failure in failures:
            print(f" - {failure}", file=sys.stderr)
        return 1

    print("Clinical image UI policy verified: no runtime remote loading, crop or stretching in audited screens.")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
