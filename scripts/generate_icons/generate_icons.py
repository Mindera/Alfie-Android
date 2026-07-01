#!/usr/bin/env python3
"""
Generate AlfieIcons.kt from VectorDrawable XMLs in res/drawable/.

Scans designsystem/src/main/res/drawable/ for ic_*.xml files and emits a
single-object AlfieIcons.kt that maps PascalCase names to @DrawableRes Int values.

Usage:
    python3 scripts/generate_icons/generate_icons.py

Adding a new icon: drop ic_<name>.xml into res/drawable/ and re-run.
"""

import os
from pathlib import Path

SCRIPT_DIR = Path(__file__).parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent

DRAWABLE_DIR = PROJECT_ROOT / "designsystem/src/main/res/drawable"
OUTPUT_FILE = PROJECT_ROOT / "designsystem/src/main/java/com/mindera/alfie/designsystem/icons/AlfieIcons.kt"

# Filename prefixes that identify legacy (non-Figma-vocabulary) drawables to skip.
SKIP_PREFIXES = ("ic_action_", "ic_informational_", "ic_modal_action_", "ic_alfie_")

HEADER = """\
// GENERATED — do not edit. Produced by scripts/generate_icons/generate_icons.py
package com.mindera.alfie.designsystem.icons

import com.mindera.alfie.designsystem.R

object AlfieIcons {\
"""

FOOTER = "}\n"


def resource_name(filename: str) -> str:
    """Return the Android resource name (filename without extension)."""
    return Path(filename).stem


def kotlin_name(filename: str) -> str:
    """Derive PascalCase Kotlin property name from a drawable filename.

    ic_bag__fill_.xml  →  BagFill
    ic_star__half_fill_.xml  →  StarHalfFill
    ic_grid_1.xml  →  Grid1
    """
    stem = Path(filename).stem  # e.g. "ic_bag__fill_"
    # Strip the leading "ic_" prefix
    if stem.startswith("ic_"):
        stem = stem[3:]
    # Split by underscore, drop empty segments (handles double/trailing underscores)
    parts = [p for p in stem.split("_") if p]
    # Capitalise each part; digits stay as-is
    return "".join(p.capitalize() for p in parts)


def collect_icons():
    """Return sorted list of (kotlin_name, resource_name) pairs."""
    icons = []
    for entry in sorted(os.listdir(DRAWABLE_DIR)):
        if not entry.endswith(".xml"):
            continue
        if not entry.startswith("ic_"):
            continue
        if any(entry.startswith(prefix) for prefix in SKIP_PREFIXES):
            continue
        icons.append((kotlin_name(entry), resource_name(entry)))
    icons.sort(key=lambda x: x[0])
    return icons


def emit(icons):
    lines = [HEADER]
    for name, res in icons:
        lines.append(f"    val {name} = R.drawable.{res}")
    lines.append(FOOTER)
    return "\n".join(lines)


def main():
    icons = collect_icons()
    if not icons:
        print("No ic_*.xml files found — nothing to generate.")
        return

    content = emit(icons)
    OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT_FILE.write_text(content, encoding="utf-8")
    print(f"Generated {len(icons)} icon(s) → {OUTPUT_FILE.relative_to(PROJECT_ROOT)}")
    for name, res in icons:
        print(f"  {name} = R.drawable.{res}")


if __name__ == "__main__":
    main()
