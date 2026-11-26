#!/usr/bin/env python3
"""
Seed-anchor code generator: reads the Mock BFF's published seed anchors from
src/mock/fixtures/seed-anchors.json in an Alfie-BFF checkout and emits the
`SeedAnchors` Kotlin object into :core:test-seed.

Shape: one `object SeedAnchors`, a nested object per JSON group. Groups are
PascalCase; leaf names stay exactly the BFF's. Strings become `const val`s,
string arrays `List<String>`s.

Run: python3 scripts/generate_seed/generate_seed_anchors.py
Re-run when a test needs an anchor the BFF has added.
"""

import argparse
import difflib
import json
import os
import re
import subprocess
import sys
from pathlib import Path

# ---------------------------------------------------------------------------
# Paths
# ---------------------------------------------------------------------------
REPO_ROOT = Path(__file__).resolve().parent.parent.parent
# The sibling Alfie-BFF checkout; ALFIE_BFF_PATH points elsewhere (e.g. from a worktree).
DEFAULT_BFF_DIR = REPO_ROOT.parent / "Alfie-BFF"
ANCHORS_IN_BFF = Path("src") / "mock" / "fixtures" / "seed-anchors.json"
OUT_FILE = (
    REPO_ROOT / "core" / "test-seed" / "src" / "main" / "kotlin"
    / "com" / "mindera" / "alfie" / "core" / "testseed" / "SeedAnchors.kt"
)

PACKAGE = "com.mindera.alfie.core.testseed"
INDENT = "    "
SCRIPT = "scripts/generate_seed/generate_seed_anchors.py"
RUN_COMMAND = f"python3 {SCRIPT}"

GEN_HEADER = (
    f"// GENERATED — do not edit. Produced by {SCRIPT}\n"
    "// Source: src/mock/fixtures/seed-anchors.json in Mindera/Alfie-BFF @ {source}\n"
    "// Regenerate when a test needs an anchor the BFF has added: see the README's \"Seed anchors\".\n"
    "// Leaf names are exactly the BFF's, as listed in its `docs/mock-platform.md`; groups are PascalCase.\n"
)

PREAMBLE = f"""
package {PACKAGE}

/**
 * The Mock BFF's **seed anchors**: the only catalogue values a test may pin to.
 *
 * Mirrors `src/mock/fixtures/seed-anchors.json` in Alfie-BFF. What each anchor promises is in that
 * repo's `docs/mock-platform.md`. Anything else in the catalogue can change at any time.
 */
"""

# ---------------------------------------------------------------------------
# Rendering
# ---------------------------------------------------------------------------

_KOTLIN_ESCAPES = {"\\": "\\\\", '"': '\\"', "$": "\\$", "\n": "\\n", "\r": "\\r", "\t": "\\t"}


def _kotlin_string(value):
    return '"' + "".join(_KOTLIN_ESCAPES.get(ch, ch) for ch in value) + '"'


class AnchorError(ValueError):
    """The anchor JSON holds something SeedAnchors has no Kotlin shape for."""


_IDENTIFIER_RE = re.compile(r"[A-Za-z_][A-Za-z0-9_]*")
# Kotlin's hard keywords: not usable as names without backticks. Soft keywords (`default`) are fine.
_HARD_KEYWORDS = {
    "as", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in",
    "interface", "is", "null", "object", "package", "return", "super", "this", "throw", "true",
    "try", "typealias", "typeof", "val", "var", "when", "while",
}


def _entry(path, value, indent):
    """The Kotlin lines declaring the anchor (or group of anchors) at `path`, at `indent`."""
    name = path[-1]
    where = ".".join(path)
    if not _IDENTIFIER_RE.fullmatch(name) or name in _HARD_KEYWORDS:
        raise AnchorError(f"'{where}' is not a plain Kotlin identifier")
    pad = INDENT * indent
    if isinstance(value, str):
        return [f"{pad}const val {name} = {_kotlin_string(value)}"]
    if isinstance(value, list):
        if not value or not all(isinstance(item, str) for item in value):
            raise AnchorError(f"'{where}' must be a non-empty array of strings")
        if len(value) == 1:
            return [f"{pad}val {name} = listOf({_kotlin_string(value[0])})"]
        items = [f"{pad}{INDENT}{_kotlin_string(item)}" for item in value]
        return [f"{pad}val {name} = listOf("] + _join_commas(items) + [f"{pad})"]
    if isinstance(value, dict):
        body = [line for child, v in value.items() for line in _entry(path + [child], v, indent + 1)]
        return [f"{pad}object {name[0].upper()}{name[1:]} {{"] + body + [f"{pad}}}"]
    raise AnchorError(f"'{where}' must be a string, an array of strings or a group")


def _join_commas(items):
    return [item + "," for item in items[:-1]] + items[-1:]


def render(anchors, bff_source):
    """The full SeedAnchors.kt text for `anchors`, read from Alfie-BFF at `bff_source` (a commit)."""
    if not isinstance(anchors, dict):
        raise AnchorError("the top level must be an object of anchors and groups")
    lines = ["object SeedAnchors {"]
    previous = None
    for name, value in anchors.items():
        entry = _entry([name], value, 1)
        # Top-level single-line anchors sit together; groups and multi-line lists get air.
        if previous is None or len(previous) > 1 or len(entry) > 1:
            lines.append("")
        lines += entry
        previous = entry
    lines.append("}")
    return GEN_HEADER.format(source=bff_source) + PREAMBLE + "\n".join(lines) + "\n"


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def _bff_dir():
    configured = os.environ.get("ALFIE_BFF_PATH")
    return Path(configured).expanduser().resolve() if configured else DEFAULT_BFF_DIR


def _git(bff_dir, *args):
    return subprocess.run(
        ["git", "-C", str(bff_dir), *args], check=True, capture_output=True, text=True,
    ).stdout.strip()


def _bff_source(bff_dir):
    """The BFF commit the anchors were read at, flagged when the JSON differs from it."""
    commit = _git(bff_dir, "rev-parse", "HEAD")
    if _git(bff_dir, "status", "--porcelain", "--", str(ANCHORS_IN_BFF)):
        return f"{commit} with uncommitted changes to {ANCHORS_IN_BFF.name}"
    return commit


def _warn(message):
    print(f"⚠ {message}", file=sys.stderr)


def _remote_main(bff_dir):
    """origin's main, read live like scripts/run-bff-tests.sh does, or None if it can't be read."""
    env = {
        **os.environ,
        "GIT_TERMINAL_PROMPT": "0",
        "GIT_SSH_COMMAND": "ssh -o BatchMode=yes -o ConnectTimeout=5",
    }
    try:
        refs = subprocess.run(
            ["git", "-C", str(bff_dir), "ls-remote", "origin", "refs/heads/main"],
            capture_output=True, text=True, env=env, timeout=30,
        ).stdout
    except (OSError, subprocess.TimeoutExpired):
        return None
    return refs.split("\t", 1)[0] or None


def _warn_if_bff_differs_from_main(bff_dir):
    """Warns, never fails, when the anchors come from somewhere other than the BFF's main.

    Mirrors `warn_if_bff_checkout_differs_from_main` in scripts/run-bff-tests.sh. A file generated
    from a BFF branch can hold anchors main doesn't have yet, and the header names only a commit.
    """
    head = _git(bff_dir, "rev-parse", "HEAD")
    branch = subprocess.run(
        ["git", "-C", str(bff_dir), "symbolic-ref", "--short", "-q", "HEAD"],
        capture_output=True, text=True,
    ).stdout.strip() or "a detached HEAD"
    remote_main = _remote_main(bff_dir)
    if remote_main is None:
        _warn(
            f"Couldn't read origin/main for the BFF checkout at {bff_dir}, "
            "so it isn't checked against main."
        )
    # CI checks out main's commit as a detached HEAD; that is main.
    on_main = branch == "main" or (branch == "a detached HEAD" and remote_main in (None, head))
    if not on_main:
        _warn(f"The BFF checkout at {bff_dir} is on {branch}, not main. CI tests against main.")
    if remote_main is not None and subprocess.run(
        ["git", "-C", str(bff_dir), "merge-base", "--is-ancestor", remote_main, "HEAD"],
        capture_output=True,
    ).returncode != 0:
        _warn(
            f"The BFF checkout at {bff_dir} is behind origin/main ({remote_main}). "
            "CI tests against main."
        )


def _without_header(kotlin):
    """`kotlin` without its leading comment header, which names the BFF commit.

    Staleness is about the anchors, not the commit: a BFF commit that leaves the JSON alone must not
    make the committed file look stale.
    """
    lines = kotlin.splitlines(keepends=True)
    while lines and lines[0].startswith("//"):
        lines.pop(0)
    return lines


def _check(output, kotlin, bff_dir):
    committed = output.read_text(encoding="utf-8") if output.exists() else ""
    diff = list(difflib.unified_diff(
        _without_header(committed), _without_header(kotlin),
        fromfile=str(output), tofile=f"generated from {bff_dir}",
    ))
    if not diff:
        print(f"✓ {output.name} is up to date with {bff_dir}")
        return 0
    print(f"⚠ {output.name} is stale against {bff_dir}. Regenerate it with:")
    print(f"    {RUN_COMMAND}\n")
    print("".join(diff), end="")
    return 1


def _fail(message):
    print(f"✗ {message}", file=sys.stderr)
    return 2


def main(argv=None):
    """Runs the CLI. Any crash exits 2, an error, never 1: callers read 1 as "stale"."""
    try:
        return _main(argv)
    except Exception as e:  # noqa: BLE001 - an uncaught exception would exit 1, which means stale
        return _fail(f"Unexpected {type(e).__name__}: {e}")


def _main(argv):
    parser = argparse.ArgumentParser(
        description="Generate SeedAnchors.kt from the Mock BFF's seed-anchors.json.",
        epilog="Exit codes: 0 written or up to date; 1 stale (--check only); 2 error.",
    )
    parser.add_argument(
        "--output", type=Path, default=OUT_FILE,
        help="the SeedAnchors.kt to write (default: the committed one in :core:test-seed)",
    )
    parser.add_argument(
        "--check", action="store_true",
        help="report whether the file is stale against the BFF checkout, without writing it",
    )
    parser.add_argument(
        "--no-branch-warning", action="store_true",
        help="don't warn when the BFF checkout isn't origin's main "
             "(for callers that already do, like scripts/run-bff-tests.sh)",
    )
    args = parser.parse_args(argv)

    bff_dir = _bff_dir()
    anchors_json = bff_dir / ANCHORS_IN_BFF
    if not anchors_json.is_file():
        return _fail(
            f"No seed anchors at {anchors_json}.\n"
            f"  Point ALFIE_BFF_PATH at an Alfie-BFF checkout (default: {DEFAULT_BFF_DIR})."
        )
    try:
        anchors = json.loads(anchors_json.read_text(encoding="utf-8"))
        kotlin = render(anchors, _bff_source(bff_dir))
    except (json.JSONDecodeError, AnchorError) as e:
        return _fail(f"{anchors_json}: {e}")
    except subprocess.CalledProcessError as e:
        return _fail(f"Can't read the commit of {bff_dir} (ALFIE_BFF_PATH): {e.stderr.strip()}")
    except OSError as e:  # e.g. no git on PATH; must not exit 1, which means stale
        return _fail(str(e))
    if not args.no_branch_warning:
        _warn_if_bff_differs_from_main(bff_dir)
    if args.check:
        return _check(args.output, kotlin, bff_dir)
    args.output.write_text(kotlin, encoding="utf-8")
    print(f"✓ Wrote {args.output}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
