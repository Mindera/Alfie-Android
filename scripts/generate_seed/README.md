# Seed Anchor Code Generator

Reads the Mock BFF's seed anchors from `src/mock/fixtures/seed-anchors.json` in an Alfie-BFF
checkout and emits the `SeedAnchors` Kotlin object into
`core/test-seed/src/main/kotlin/com/mindera/alfie/core/testseed/SeedAnchors.kt`.

## Requirements

Python 3 (stdlib only — no external packages needed) and `git`, which the generator uses to read
the BFF commit it records in the file's header.

## Usage

From the repo root:

```bash
python3 scripts/generate_seed/generate_seed_anchors.py
```

The BFF checkout is `${ALFIE_BFF_PATH:-../Alfie-BFF}`. `../Alfie-BFF` is the sibling of the repo
root, so from a worktree set `ALFIE_BFF_PATH`:

```bash
ALFIE_BFF_PATH=~/workspace/Alfie/Alfie-BFF python3 scripts/generate_seed/generate_seed_anchors.py
```

Then build the module to verify the generated code compiles:

```bash
./gradlew :core:test-seed:compileDebugKotlin
```

### Checking for staleness

```bash
python3 scripts/generate_seed/generate_seed_anchors.py --check
```

Writes nothing. Compares the committed file with what the generator would produce from the BFF
checkout, ignoring the header, so a BFF commit that leaves the anchors alone isn't staleness.

| Exit code | Meaning |
|---|---|
| `0` | Up to date (or, without `--check`, written) |
| `1` | Stale: the diff is printed, with the command to regenerate |
| `2` | Error: no anchor JSON at `ALFIE_BFF_PATH`, not a git checkout, no `git`, or JSON with no Kotlin shape |

Staleness is a warning, never a failure: the BFF only ever adds anchors, so an older file stays
correct. Regenerate when a test needs a new anchor.

## What gets generated

One `object SeedAnchors`, with a nested object per JSON group:

| JSON | Kotlin |
|---|---|
| `"unknownBarcode": "0000000000000"` | `const val unknownBarcode = "0000000000000"` |
| `"menuHandleAliases": ["product-category"]` | `val menuHandleAliases = listOf("product-category")` |
| `"products": { "default": "…" }` | `object Products { const val default = "…" }` |

Groups are PascalCase; leaf names stay exactly the BFF's, so a name in a test is a plain-text search
away in the BFF's `docs/mock-platform.md`. Keys keep the JSON's order. A key that isn't a plain
Kotlin identifier, or a value that isn't a string, a non-empty string array or a group, stops the
generator with the anchor's path.

If the JSON has uncommitted changes, the header says so next to the commit.

## Tests

```bash
python3 -m unittest discover -s scripts/generate_seed
```

`testdata/` holds a verbatim copy of Alfie-BFF `main`'s `seed-anchors.json` and the output the
generator must produce from it: the hand-written `SeedAnchors.kt` it replaced, under the generated
header for a fake commit.
