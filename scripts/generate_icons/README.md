# generate_icons

Generates `AlfieIcons.kt` from VectorDrawable XML files in `res/drawable/`.

## Requirements

Python 3 — stdlib only, no extra installs.

## Usage

```bash
python3 scripts/generate_icons/generate_icons.py
```

## What it does

1. Scans `designsystem/src/main/res/drawable/` for `ic_*.xml` files
2. Skips legacy-named drawables (`ic_action_*`, `ic_informational_*`, `ic_modal_action_*`, `ic_alfie_*`)
3. Derives a PascalCase Kotlin property name from each filename:
   - `ic_chevron_down.xml` → `ChevronDown`
   - `ic_bag__fill_.xml` → `BagFill` (double-underscore from Figma export is handled)
   - `ic_star__half_fill_.xml` → `StarHalfFill`
4. Emits `designsystem/src/main/java/com/mindera/alfie/designsystem/icons/AlfieIcons.kt`

## Adding a new icon (re-export workflow)

1. Open the [Figma Iconography page](https://www.figma.com/design/PWVgEoKrIw9Hv7QlOCcUoq/Alfie---Design-System?node-id=3001-7582)
2. Select the icon node(s) to add or update
   - For OS-specific icons (Back, Share): select only the `OS=Android` variant frame
   - For fill variants: select the `(Fill)` frame — Figma names it `Icon (Fill)`, export produces `ic_icon__fill_.xml` after import
3. Export as SVG locally (24×24, 1× scale); use the Figma name (e.g., `Bag (Fill).svg`)
4. Import into Android Studio: **File → New → Vector Asset → Local file**
   - Set the output name to `ic_<figma_name_snake_case>` (e.g., `ic_bag`, `ic_bag__fill_`)
   - Output module: `designsystem`, directory: `res/drawable/`
   - Verify the stroke colour remains `#000000` (tinting is applied at call sites)
5. Discard the intermediate SVG file — do not commit it
6. Run the generator:
   ```bash
   python3 scripts/generate_icons/generate_icons.py
   ```
7. Verify compilation:
   ```bash
   ./gradlew :designsystem:compileDebugKotlin
   ```
8. Commit the new drawable(s) + regenerated `AlfieIcons.kt`

## Output

`designsystem/src/main/java/com/mindera/alfie/designsystem/icons/AlfieIcons.kt`

Single `object AlfieIcons` with one `val` per icon mapping the PascalCase name to a `@DrawableRes Int`:

```kotlin
object AlfieIcons {
    val Bag      = R.drawable.ic_bag
    val BagFill  = R.drawable.ic_bag__fill_
    // ...
}
```

## Using icons in Compose

```kotlin
// Outline icon with token-backed size and tint
Icon(
    painter = painterResource(AlfieIcons.ChevronRight),
    contentDescription = null,
    modifier = Modifier.size(LocalTheme.current.sizing.icon.medium),
    tint = LocalTheme.current.color.content.contentPrimary,
)
```
