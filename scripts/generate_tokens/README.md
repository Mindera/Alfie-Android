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
   ../Alfie-Mobile-Design-Tokens/design-tokens/.primitives.alfie-theme.tokens.json \
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

## Token resolution

The generator resolves tokens using the **Android / Small-screen** platform profile:
- Platform: `system.android.tokens.json`
- Screen size: `screen-size.small-(s).tokens.json`
- Allow-listed cycles and broken refs are handled per `.cycle-allowlist.json` / `.broken-ref-allowlist.json`.

Font families are currently placeholder `FontFamily.Default` — see `TODO(fonts):` comments in the generated files.
