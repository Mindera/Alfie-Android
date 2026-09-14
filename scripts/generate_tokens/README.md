# Design Token Code Generator

Reads the W3C DTCG JSON files from `designsystem/src/main/assets/design_tokens/` and
emits type-safe Kotlin into `designsystem/src/main/java/com/mindera/alfie/designsystem/tokens/`.

## Requirements

Python 3 (stdlib only — no external packages needed).

## Usage

From the repo root:

```bash
python3 scripts/generate_tokens/generate_design_tokens.py
```

Then build the module to verify the generated code compiles:

```bash
./gradlew :designsystem:compileDebugKotlin
```

## Updating tokens

Run this when design tokens change in `Mindera/Alfie-Mobile-Design-Tokens`:

```bash
# 1. Pull latest tokens from sibling repo
git -C ../Alfie-Mobile-Design-Tokens checkout main && git -C ../Alfie-Mobile-Design-Tokens pull --ff-only

# 2. Copy updated JSON files into the app repo
cp ../Alfie-Mobile-Design-Tokens/design-tokens/*.json \
   ../Alfie-Mobile-Design-Tokens/design-tokens/.primitives.*.tokens.json \
   ../Alfie-Mobile-Design-Tokens/design-tokens/.documentation.mode-1.tokens.json \
   ../Alfie-Mobile-Design-Tokens/.cycle-allowlist.json \
   ../Alfie-Mobile-Design-Tokens/.broken-ref-allowlist.json \
   designsystem/src/main/assets/design_tokens/

# 3. Re-run the generator
python3 scripts/generate_tokens/generate_design_tokens.py

# 4. Verify it compiles
./gradlew :designsystem:compileDebugKotlin

# 5. Commit the updated JSON + regenerated Kotlin
git add designsystem/src/main/assets/design_tokens/ \
        designsystem/src/main/java/com/mindera/alfie/designsystem/tokens/
git commit -m "chore: update design tokens from Alfie-Mobile-Design-Tokens"
```

## What gets generated

All files land in `com.mindera.alfie.designsystem.tokens` alongside `NewTheme.kt`.

| File | Object | Contents |
|---|---|---|
| `Colors.kt` | `Colors` | Internal `Primitives` + public semantic color groups (`surface`, `content`, `link`, `button`, `border`) |
| `Typography.kt` | `Typography` | 15 `TextStyle` entries grouped by Figma vocabulary (`display`, `heading`, `body`, `link`, `label`) |
| `Spacing.kt` | `Spacing` | All spacing values in `Dp` |
| `Sizing.kt` | `Sizing` | Icon sizes, radii, and interactive paddings in `Dp`/`RoundedCornerShape` |

## Brand mode

The four brand-scoped collections — `.primitives`, `theme`, `sizing`, `typography` — are named
after the Figma brand mode (`.primitives.<mode>.tokens.json`), and that mode is renamed whenever
the brand is (`alfie-theme` → `selfridges-theme`). The generator reads the current name out of
`manifest.json` rather than hardcoding one, and prints it on each run:

```
  Brand mode: selfridges-theme
```

Two consequences when a rename lands:

- The glob in step 2 copies the new files, but the **previous brand's files stay behind**. Delete
  any file in `assets/design_tokens/` that `manifest.json` no longer references — they are dead
  weight and mislead the next reader.
- The generator fails loudly if `manifest.json` lists more than one mode for those collections; a
  genuinely multi-brand export needs it taught which brand to emit.

## Token resolution

The generator resolves tokens using the **Android / Small-screen** platform profile:
- Platform: `system.android.tokens.json`
- Screen size: `screen-size.small-(s).tokens.json`
- Allow-listed cycles and broken refs are handled per `.cycle-allowlist.json` / `.broken-ref-allowlist.json`.

### Fonts

`designsystem/src/main/assets/font/` is **gitignored** — it is a local staging area, not a
committed source. Each family needs its own folder named after the font family (spaces →
underscores) containing `static/<Family>-<Weight>.ttf`, e.g. `Avalon/static/Avalon-Regular.ttf`.
The generator imports those into `designsystem/src/main/res/font/` and emits the `FontFamily`
code; only the imported `res/font` copies are committed.

This means **a fresh clone cannot run the generator** until the staging folders are populated —
get the TTFs from design. Only the weights the typography styles actually reference are required;
the rest of the standard set is imported opportunistically and skipped silently when absent.
