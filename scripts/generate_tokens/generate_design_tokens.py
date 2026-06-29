#!/usr/bin/env python3
"""
Design-token code generator (interface-based): reads W3C DTCG JSON from
designsystem/src/main/assets/design_tokens/ and emits type-safe Kotlin
@Immutable interfaces + concrete instances into .../tokens/.

Architecture:
  LightPrimitives (raw literals) → DefaultColors / DefaultSizing /
  DefaultTypographyTokens (refs only) → DefaultTypography (TextStyles)
  All wired through NewTheme + LocalTheme CompositionLocal.

Run: python3 scripts/generate_tokens/generate_design_tokens.py
Re-run whenever design-token JSON files are updated.
"""
import json
import re
import sys
from pathlib import Path

# ---------------------------------------------------------------------------
# Paths
# ---------------------------------------------------------------------------
REPO_ROOT = Path(__file__).resolve().parent.parent.parent
TOKENS_DIR = REPO_ROOT / "designsystem" / "src" / "main" / "assets" / "design_tokens"
OUT_DIR = (
    REPO_ROOT / "designsystem" / "src" / "main" / "java"
    / "com" / "mindera" / "alfie" / "designsystem" / "tokens"
)

PACKAGE = "com.mindera.alfie.designsystem.tokens"
GEN_HEADER = (
    "// GENERATED — do not edit. "
    "Produced by scripts/generate_tokens/generate_design_tokens.py\n"
    "// Source: designsystem/src/main/assets/design_tokens "
    "(Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.\n"
)

DOC_PREFIX = "~~doc-"
REF_RE = re.compile(r"^\{(.+)\}$")

# ---------------------------------------------------------------------------
# Loading
# ---------------------------------------------------------------------------

def _load(path):
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def _load_tokens(path):
    """Load a token file, returning only non-doc entries that have a $value."""
    raw = _load(path)
    return {
        name: obj
        for name, obj in raw.items()
        if not name.startswith(DOC_PREFIX)
        and not name.startswith("$")
        and isinstance(obj, dict)
        and "$value" in obj
    }


# ---------------------------------------------------------------------------
# Value conversion helpers
# ---------------------------------------------------------------------------

def _to8(f):
    return max(0, min(255, round(f * 255)))


def color_to_kotlin(value):
    """Convert resolved color dict → Color(0xAARRGGBB)."""
    comps = value["components"]
    alpha = value.get("alpha", 1.0)
    aa = _to8(alpha)
    rr = _to8(comps[0])
    gg = _to8(comps[1])
    bb = _to8(comps[2])
    return f"Color(0x{aa:02X}{rr:02X}{gg:02X}{bb:02X})"


def dim_to_dp(value):
    """Resolved dimension value → N.dp literal."""
    v = value["value"] if isinstance(value, dict) else float(value)
    iv = int(v)
    return f"{iv}.dp" if iv == v else f"{v}.dp"


def dim_to_sp(value):
    """Resolved dimension value → N.sp literal."""
    v = value["value"] if isinstance(value, dict) else float(value)
    iv = int(v)
    if v < 0:
        return f"({iv}).sp" if iv == v else f"({v}).sp"
    return f"{iv}.sp" if iv == v else f"{v}.sp"


FONT_WEIGHT_MAP = {
    "Regular": "FontWeight.Normal",
    "Medium": "FontWeight.W500",
    "SemiBold": "FontWeight.W600",
    "Bold": "FontWeight.Bold",
}


def font_weight_to_kotlin(w):
    return FONT_WEIGHT_MAP.get(w, "FontWeight.Normal")


# ---------------------------------------------------------------------------
# Name helpers
# ---------------------------------------------------------------------------

def _to_camel(s):
    """'x-small' → 'xSmall'"""
    parts = s.split("-")
    return parts[0] + "".join(p.capitalize() for p in parts[1:])


def _to_pascal(s):
    return s[0].upper() + s[1:] if s else s


def prim_color_member(name):
    """'colours-neutrals-900' → 'neutrals900', 'colours-transparent-transparent' → 'transparent'"""
    rest = name[len("colours-"):]
    if rest == "transparent-transparent":
        return "transparent"
    return _to_camel(rest)


def prim_spacing_member(name):
    """'spacing-spacing-16' → 'spacing16'"""
    return "spacing" + name.split("-")[-1]


def prim_font_family_member(name):
    """'typography-font-family-primary-android' → 'primaryAndroid'"""
    rest = name[len("typography-font-family-"):]
    return _to_camel(rest)


def prim_font_size_member(name):
    """'typography-font-size-font-size-16' → 'fontSize16'"""
    rest = name[len("typography-font-size-"):]
    return _to_camel(rest)


def prim_line_height_member(name):
    """'typography-line-height-line-height-16' → 'lineHeight16'"""
    rest = name[len("typography-line-height-"):]
    return _to_camel(rest)


def prim_kerning_member(name):
    """'typography-kerning-tight' → 'tight'"""
    return name[len("typography-kerning-"):]


def sizing_nav(name):
    """Return navPath (group.member) for a sizing token."""
    if name.startswith("icons-icon-"):
        member = name[len("icons-icon-"):]
        return f"icon.{member}"
    if name.startswith("radius-"):
        member = name[len("radius-"):]
        return f"radius.{member}"
    if name.startswith("interactive-"):
        member = _to_camel(name[len("interactive-"):])
        return f"interactive.{member}"
    return name


def theme_color_nav(name):
    """'surface-background-primary' → 'surface.backgroundPrimary'"""
    parts = name.split("-", 1)
    group = parts[0]
    member = _to_camel(parts[1]) if len(parts) > 1 else "value"
    return f"{group}.{member}"


def typo_field_nav(name):
    """
    'display-large-font-size' → 'displayLarge.fontSize'
    'body-medium-strikethrough-font-family' → 'bodyMediumStrikethrough.fontFamily'
    """
    for suffix, field in [
        ("-font-family", "fontFamily"),
        ("-font-weight", "fontWeight"),
        ("-font-size", "fontSize"),
        ("-line-height", "lineHeight"),
        ("-kerning", "kerning"),
    ]:
        if name.endswith(suffix):
            style_part = name[:-len(suffix)]
            return f"{_to_camel(style_part)}.{field}"
    return name


# ---------------------------------------------------------------------------
# EMITTED_REGISTRY and WALK map
# ---------------------------------------------------------------------------

def build_emitted_registry(primitives, theme_colors, sizing, typo_tokens):
    """Build EMITTED_REGISTRY: token_name → ('layer', 'navPath')"""
    reg = {}

    for name in primitives:
        if name.startswith("colours-"):
            reg[name] = ("primitive", f"colors.{prim_color_member(name)}")
        elif name.startswith("spacing-spacing-"):
            reg[name] = ("primitive", f"spacing.{prim_spacing_member(name)}")
        elif name.startswith("typography-font-family-"):
            reg[name] = ("primitive", f"typography.fontFamily.{prim_font_family_member(name)}")
        elif name.startswith("typography-font-size-"):
            reg[name] = ("primitive", f"typography.fontSize.{prim_font_size_member(name)}")
        elif name.startswith("typography-line-height-"):
            reg[name] = ("primitive", f"typography.lineHeight.{prim_line_height_member(name)}")
        elif name.startswith("typography-kerning-"):
            reg[name] = ("primitive", f"typography.kerning.{prim_kerning_member(name)}")
        elif name == "border-border-weight-default":
            reg[name] = ("primitive", "border.weightDefault")

    for name in theme_colors:
        reg[name] = ("themeColor", theme_color_nav(name))

    for name in sizing:
        reg[name] = ("themeSizing", sizing_nav(name))

    for name in typo_tokens:
        reg[name] = ("typoToken", typo_field_nav(name))

    return reg


def build_walk_map(tokens_dir):
    """Load screen-size.small-(s) + system.android → WALK map (android values win on collision)."""
    walk = {}
    walk.update(_load_tokens(tokens_dir / "screen-size.small-(s).tokens.json"))
    walk.update(_load_tokens(tokens_dir / "system.android.tokens.json"))
    return walk


# ---------------------------------------------------------------------------
# Reference resolution
# ---------------------------------------------------------------------------

def resolve_ref(target, reg, walk, visited=None):
    """Resolve target → (layer, navPath).

    Stops at the first emitted token (preserving chains).
    Walks through the WALK map for screen-size/android aliases.
    Returns ('primitive'|'themeColor'|'themeSizing'|'typoToken', navPath).
    """
    if visited is None:
        visited = frozenset()

    if target in reg:
        return reg[target]

    if target in visited:
        raise RuntimeError(f"Cycle during resolution involving '{target}'")

    if target not in walk:
        raise RuntimeError(
            f"Cannot resolve '{target}': not in emitted registry and not in WALK map"
        )

    entry = walk[target]
    val = entry["$value"]
    if isinstance(val, str):
        m = REF_RE.match(val)
        if m:
            return resolve_ref(m.group(1), reg, walk, visited | {target})

    raise RuntimeError(
        f"Walk entry '{target}' has a non-ref or unexpected raw value: {val!r}"
    )


def to_expr(layer, nav, context):
    """Turn a resolve_ref result into a Kotlin expression."""
    if layer == "primitive":
        return f"primitive.{nav}"
    if layer == "themeColor":
        return nav  # self-access inside DefaultColors
    if layer == "themeSizing":
        return nav  # self-access inside DefaultSizing (shouldn't appear cross-layer)
    if layer == "typoToken":
        if context == "DefaultTypographyTokens":
            return nav  # self-access
        if context == "DefaultTypography":
            return f"tokens.{nav}"
        return nav
    return nav


# ---------------------------------------------------------------------------
# Primitive raw value resolver (for LightPrimitives raw literals)
# ---------------------------------------------------------------------------

def resolve_prim_raw(name, primitives):
    """Follow any one-hop internal ref within primitives to get the raw value.

    Font sizes / line heights in the primitives file alias spacing tokens.
    Since Dp ≠ sp, we follow the ref and emit the numeric value as raw sp.
    """
    entry = primitives[name]
    val = entry["$value"]
    if isinstance(val, str):
        m = REF_RE.match(val)
        if m:
            target = m.group(1)
            if target in primitives:
                return resolve_prim_raw(target, primitives)
            raise RuntimeError(
                f"Primitive '{name}' refs '{target}' which is not in primitives"
            )
    return val


# ---------------------------------------------------------------------------
# Emitter: Primitives.kt
# ---------------------------------------------------------------------------

def emit_primitives(primitives):
    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.runtime.Immutable\n",
        "import androidx.compose.ui.graphics.Color\n",
        "import androidx.compose.ui.text.font.FontFamily\n",
        "import androidx.compose.ui.unit.Dp\n",
        "import androidx.compose.ui.unit.TextUnit\n",
        "import androidx.compose.ui.unit.dp\n",
        "import androidx.compose.ui.unit.sp\n",
    ]

    color_names = sorted(
        (n for n in primitives if n.startswith("colours-")),
    )
    spacing_names = sorted(
        (n for n in primitives if n.startswith("spacing-spacing-")),
        key=lambda x: float(x.split("-")[-1]),
    )
    ff_names = sorted(n for n in primitives if n.startswith("typography-font-family-"))
    fs_names = sorted(
        (n for n in primitives if n.startswith("typography-font-size-font-size-")),
        key=lambda x: float(x.split("-")[-1]),
    )
    lh_names = sorted(
        (n for n in primitives if n.startswith("typography-line-height-line-height-")),
        key=lambda x: float(x.split("-")[-1]),
    )
    k_names = sorted(n for n in primitives if n.startswith("typography-kerning-"))

    # ---- Interfaces ----

    lines.append("\n@Immutable\ninterface Primitives {\n")
    lines.append("    val colors: PrimitiveColors\n")
    lines.append("    val spacing: PrimitiveSpacing\n")
    lines.append("    val typography: PrimitiveTypography\n")
    lines.append("    val border: PrimitiveBorder\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveColors {\n")
    for n in color_names:
        lines.append(f"    val {prim_color_member(n)}: Color\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveSpacing {\n")
    for n in spacing_names:
        lines.append(f"    val {prim_spacing_member(n)}: Dp\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveFontFamilies {\n")
    for n in ff_names:
        lines.append(f"    val {prim_font_family_member(n)}: FontFamily\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveFontSizes {\n")
    for n in fs_names:
        lines.append(f"    val {prim_font_size_member(n)}: TextUnit\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveLineHeights {\n")
    for n in lh_names:
        lines.append(f"    val {prim_line_height_member(n)}: TextUnit\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveKernings {\n")
    for n in k_names:
        lines.append(f"    val {prim_kerning_member(n)}: TextUnit\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveTypography {\n")
    lines.append("    val fontFamily: PrimitiveFontFamilies\n")
    lines.append("    val fontSize: PrimitiveFontSizes\n")
    lines.append("    val lineHeight: PrimitiveLineHeights\n")
    lines.append("    val kerning: PrimitiveKernings\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface PrimitiveBorder {\n")
    lines.append("    val weightDefault: Dp\n")
    lines.append("}\n")

    # ---- LightPrimitives ----

    lines.append("\n@Immutable\nobject LightPrimitives : Primitives {\n")

    lines.append("    override val colors = object : PrimitiveColors {\n")
    for n in color_names:
        val = resolve_prim_raw(n, primitives)
        lines.append(f"        override val {prim_color_member(n)} = {color_to_kotlin(val)}\n")
    lines.append("    }\n")

    lines.append("    override val spacing = object : PrimitiveSpacing {\n")
    for n in spacing_names:
        val = resolve_prim_raw(n, primitives)
        lines.append(f"        override val {prim_spacing_member(n)} = {dim_to_dp(val)}\n")
    lines.append("    }\n")

    FF_TODOS = {
        "typography-font-family-brand": "Libre Baskerville",
        "typography-font-family-primary-android": "Roboto",
        "typography-font-family-primary-ios": "SF Pro",
        "typography-font-family-primary-web": "Inter",
    }

    lines.append("    override val typography = object : PrimitiveTypography {\n")

    lines.append("        override val fontFamily = object : PrimitiveFontFamilies {\n")
    for n in ff_names:
        todo = FF_TODOS.get(n, "")
        comment = f' // TODO(fonts): "{todo}"' if todo else ""
        lines.append(
            f"            override val {prim_font_family_member(n)}: FontFamily"
            f" = FontFamily.Default{comment}\n"
        )
    lines.append("        }\n")

    lines.append("        override val fontSize = object : PrimitiveFontSizes {\n")
    for n in fs_names:
        val = resolve_prim_raw(n, primitives)
        lines.append(f"            override val {prim_font_size_member(n)} = {dim_to_sp(val)}\n")
    lines.append("        }\n")

    lines.append("        override val lineHeight = object : PrimitiveLineHeights {\n")
    for n in lh_names:
        val = resolve_prim_raw(n, primitives)
        lines.append(f"            override val {prim_line_height_member(n)} = {dim_to_sp(val)}\n")
    lines.append("        }\n")

    lines.append("        override val kerning = object : PrimitiveKernings {\n")
    for n in k_names:
        val = resolve_prim_raw(n, primitives)
        lines.append(f"            override val {prim_kerning_member(n)} = {dim_to_sp(val)}\n")
    lines.append("        }\n")

    lines.append("    }\n")  # end typography

    lines.append("    override val border = object : PrimitiveBorder {\n")
    lines.append("        override val weightDefault = 1.dp\n")
    lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


# ---------------------------------------------------------------------------
# Emitter: Colors.kt
# ---------------------------------------------------------------------------

# Groups emitted in topological order so self-references within DefaultColors
# are always to already-initialized properties.
_COLOR_GROUP_ORDER = ["surface", "content", "border", "button", "link"]


def emit_colors(theme_colors, reg, walk):
    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.runtime.Immutable\n",
        "import androidx.compose.ui.graphics.Color\n",
    ]

    # Group tokens
    groups = {}
    for name in theme_colors:
        nav = theme_color_nav(name)
        group = nav.split(".")[0]
        member = nav.split(".")[1]
        groups.setdefault(group, []).append((member, name))

    ordered_groups = [g for g in _COLOR_GROUP_ORDER if g in groups]
    for g in sorted(groups):
        if g not in ordered_groups:
            ordered_groups.append(g)

    # ---- Interfaces ----
    lines.append("\n@Immutable\ninterface Colors {\n")
    for g in ordered_groups:
        lines.append(f"    val {g}: Color{_to_pascal(g)}\n")
    lines.append("}\n")

    for g in ordered_groups:
        lines.append(f"\n@Immutable\ninterface Color{_to_pascal(g)} {{\n")
        for member, _ in sorted(groups[g], key=lambda x: x[0]):
            lines.append(f"    val {member}: Color\n")
        lines.append("}\n")

    # ---- DefaultColors ----
    lines.append("\n@Immutable\nclass DefaultColors(private val primitive: Primitives) : Colors {\n")

    for g in ordered_groups:
        lines.append(f"    override val {g} = object : Color{_to_pascal(g)} {{\n")
        for member, token_name in sorted(groups[g], key=lambda x: x[0]):
            raw_val = theme_colors[token_name]["$value"]
            if isinstance(raw_val, str):
                m = REF_RE.match(raw_val)
                if m:
                    layer, nav = resolve_ref(m.group(1), reg, walk)
                    expr = to_expr(layer, nav, "DefaultColors")
                else:
                    expr = f'"{raw_val}"'
            else:
                expr = color_to_kotlin(raw_val)
            lines.append(f"        override val {member} = {expr}\n")
        lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


# ---------------------------------------------------------------------------
# Emitter: Sizing.kt
# ---------------------------------------------------------------------------

def emit_sizing(sizing, reg, walk):
    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.foundation.shape.CircleShape\n",
        "import androidx.compose.foundation.shape.RoundedCornerShape\n",
        "import androidx.compose.runtime.Immutable\n",
        "import androidx.compose.ui.graphics.Shape\n",
        "import androidx.compose.ui.unit.Dp\n",
        "import androidx.compose.ui.unit.dp\n",
    ]

    icons = {n: e for n, e in sizing.items() if n.startswith("icons-icon-")}
    radii = {n: e for n, e in sizing.items() if n.startswith("radius-")}
    interactive = {n: e for n, e in sizing.items() if n.startswith("interactive-")}

    # ---- Interfaces ----
    lines.append("\n@Immutable\ninterface Sizing {\n")
    lines.append("    val icon: SizingIcon\n")
    lines.append("    val radius: SizingRadius\n")
    lines.append("    val interactive: SizingInteractive\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface SizingIcon {\n")
    for n in sorted(icons):
        lines.append(f"    val {n[len('icons-icon-'):]}: Dp\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface SizingRadius {\n")
    for n in sorted(radii):
        lines.append(f"    val {n[len('radius-'):]}: Shape\n")
    lines.append("}\n")

    lines.append("\n@Immutable\ninterface SizingInteractive {\n")
    for n in sorted(interactive):
        lines.append(f"    val {_to_camel(n[len('interactive-'):])}: Dp\n")
    lines.append("}\n")

    # ---- DefaultSizing ----
    lines.append("\n@Immutable\nclass DefaultSizing(private val primitive: Primitives) : Sizing {\n")

    lines.append("    override val icon = object : SizingIcon {\n")
    for n in sorted(icons):
        member = n[len("icons-icon-"):]
        raw = icons[n]["$value"]
        m = REF_RE.match(raw) if isinstance(raw, str) else None
        if m:
            layer, nav = resolve_ref(m.group(1), reg, walk)
            expr = to_expr(layer, nav, "DefaultSizing")
        else:
            expr = dim_to_dp(raw)
        lines.append(f"        override val {member} = {expr}\n")
    lines.append("    }\n")

    lines.append("    override val radius = object : SizingRadius {\n")
    for n in sorted(radii):
        member = n[len("radius-"):]
        raw = radii[n]["$value"]
        if isinstance(raw, dict):
            v_num = raw["value"]
            if v_num >= 1000:
                lines.append(f"        override val {member} = CircleShape\n")
            else:
                lines.append(f"        override val {member} = RoundedCornerShape({dim_to_dp(raw)})\n")
        elif isinstance(raw, str):
            m = REF_RE.match(raw)
            if m:
                layer, nav = resolve_ref(m.group(1), reg, walk)
                expr = to_expr(layer, nav, "DefaultSizing")
                lines.append(f"        override val {member} = RoundedCornerShape({expr})\n")
    lines.append("    }\n")

    lines.append("    override val interactive = object : SizingInteractive {\n")
    for n in sorted(interactive):
        member = _to_camel(n[len("interactive-"):])
        raw = interactive[n]["$value"]
        m = REF_RE.match(raw) if isinstance(raw, str) else None
        if m:
            layer, nav = resolve_ref(m.group(1), reg, walk)
            expr = to_expr(layer, nav, "DefaultSizing")
        else:
            expr = dim_to_dp(raw)
        lines.append(f"        override val {member} = {expr}\n")
    lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


# ---------------------------------------------------------------------------
# Emitter: TypographyTokens.kt
# ---------------------------------------------------------------------------

# Topologically ordered: styles that ref siblings come after their dependencies.
_STYLE_ORDER = [
    "display-large", "display-medium", "display-small",
    "heading-large", "heading-medium", "heading-small", "heading-x-small",
    "body-large", "body-medium", "body-small",
    "label-small",
    # refs bodyMedium / bodySmall / labelSmall:
    "body-medium-strikethrough",
    "link-medium",
    "link-small",
    "label-small-bold",
]

_TYPO_FIELDS = [
    ("-font-family", "fontFamily"),
    ("-font-size", "fontSize"),
    ("-line-height", "lineHeight"),
    ("-kerning", "kerning"),
]


def emit_typography_tokens(typo_tokens, reg, walk):
    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.runtime.Immutable\n",
        "import androidx.compose.ui.text.font.FontFamily\n",
        "import androidx.compose.ui.unit.TextUnit\n",
    ]

    # ---- StyleTokens interface ----
    lines.append("\n@Immutable\ninterface StyleTokens {\n")
    lines.append("    val fontFamily: FontFamily\n")
    lines.append("    val fontSize: TextUnit\n")
    lines.append("    val lineHeight: TextUnit\n")
    lines.append("    val kerning: TextUnit\n")
    lines.append("}\n")

    # ---- TypographyTokens interface ----
    lines.append("\n@Immutable\ninterface TypographyTokens {\n")
    for style_name in _STYLE_ORDER:
        lines.append(f"    val {_to_camel(style_name)}: StyleTokens\n")
    lines.append("}\n")

    # ---- DefaultTypographyTokens ----
    lines.append(
        "\n@Immutable\nclass DefaultTypographyTokens(private val primitive: Primitives)"
        " : TypographyTokens {\n"
    )

    for style_name in _STYLE_ORDER:
        style_member = _to_camel(style_name)
        lines.append(f"    override val {style_member} = object : StyleTokens {{\n")

        for suffix, kt_field in _TYPO_FIELDS:
            token_name = style_name + suffix
            if token_name not in typo_tokens:
                print(f"  WARNING: missing token '{token_name}'", file=sys.stderr)
                continue

            raw_val = typo_tokens[token_name]["$value"]
            if isinstance(raw_val, str):
                m = REF_RE.match(raw_val)
                if m:
                    try:
                        layer, nav = resolve_ref(m.group(1), reg, walk)
                        expr = to_expr(layer, nav, "DefaultTypographyTokens")
                    except RuntimeError as exc:
                        print(f"  WARNING: {exc}", file=sys.stderr)
                        expr = "FontFamily.Default  // unresolved"
                else:
                    expr = f'"{raw_val}"'
            else:
                expr = dim_to_sp(raw_val)
            lines.append(f"        override val {kt_field} = {expr}\n")

        lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


# ---------------------------------------------------------------------------
# Emitter: Typography.kt
# ---------------------------------------------------------------------------

_TYPOGRAPHY_STYLE_NAMES = [
    "display-large", "display-medium", "display-small",
    "heading-large", "heading-medium", "heading-small", "heading-x-small",
    "body-large", "body-medium", "body-medium-strikethrough", "body-small",
    "link-medium", "link-small",
    "label-small", "label-small-bold",
]

_TYPOGRAPHY_GROUP_ORDER = ["display", "heading", "body", "link", "label"]


def emit_typography(styles_raw, reg, walk):
    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.runtime.Immutable\n",
        "import androidx.compose.ui.text.PlatformTextStyle\n",
        "import androidx.compose.ui.text.TextStyle\n",
        "import androidx.compose.ui.text.font.FontWeight\n",
    ]

    # Group styles
    style_groups = {}
    for style_name in _TYPOGRAPHY_STYLE_NAMES:
        parts = style_name.split("-", 1)
        group = parts[0]
        member = _to_camel(parts[1]) if len(parts) > 1 else "default"
        style_groups.setdefault(group, []).append((member, style_name))

    # ---- Interfaces ----
    lines.append("\n@Immutable\ninterface Typography {\n")
    for g in _TYPOGRAPHY_GROUP_ORDER:
        if g in style_groups:
            lines.append(f"    val {g}: Typography{_to_pascal(g)}\n")
    lines.append("}\n")

    for g in _TYPOGRAPHY_GROUP_ORDER:
        if g not in style_groups:
            continue
        lines.append(f"\n@Immutable\ninterface Typography{_to_pascal(g)} {{\n")
        for member, _ in style_groups[g]:
            lines.append(f"    val {member}: TextStyle\n")
        lines.append("}\n")

    # ---- DefaultTypography ----
    lines.append(
        "\n@Immutable\nclass DefaultTypography(private val tokens: TypographyTokens) : Typography {\n"
    )

    for g in _TYPOGRAPHY_GROUP_ORDER:
        if g not in style_groups:
            continue
        lines.append(f"    override val {g} = object : Typography{_to_pascal(g)} {{\n")

        for member, style_name in style_groups[g]:
            token_member = _to_camel(style_name)  # e.g. 'bodyMediumStrikethrough'
            raw = styles_raw.get(style_name)
            if raw is None:
                print(f"  WARNING: style '{style_name}' not found in styles file", file=sys.stderr)
                continue
            weight_str = raw["$value"].get("fontWeight", "Regular")
            weight_kt = font_weight_to_kotlin(weight_str)

            lines.append(f"        override val {member}: TextStyle =\n")
            lines.append(f"            TextStyle(\n")
            lines.append(f"                platformStyle = PlatformTextStyle(includeFontPadding = false),\n")
            lines.append(f"                fontFamily = tokens.{token_member}.fontFamily,\n")
            lines.append(f"                fontWeight = {weight_kt},\n")
            lines.append(f"                fontSize = tokens.{token_member}.fontSize,\n")
            lines.append(f"                lineHeight = tokens.{token_member}.lineHeight,\n")
            lines.append(f"                letterSpacing = tokens.{token_member}.kerning,\n")
            lines.append(f"            )\n")

        lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    tokens_dir = TOKENS_DIR
    if not tokens_dir.exists():
        print(f"✗ Tokens directory not found: {tokens_dir}", file=sys.stderr)
        sys.exit(1)

    print(f"Loading tokens from {tokens_dir} …")

    primitives = _load_tokens(tokens_dir / ".primitives.alfie-theme.tokens.json")
    theme_colors = _load_tokens(tokens_dir / "theme.alfie-theme.tokens.json")
    sizing = _load_tokens(tokens_dir / "sizing.alfie-theme.tokens.json")
    typo_tokens = _load_tokens(tokens_dir / "typography.alfie-theme.tokens.json")

    styles_raw_full = _load(tokens_dir / "typography.styles.tokens.json")
    styles_raw = {
        n: obj
        for n, obj in styles_raw_full.items()
        if not n.startswith(DOC_PREFIX)
    }

    print(
        f"  Loaded {len(primitives)} primitive, {len(theme_colors)} theme-color, "
        f"{len(sizing)} sizing, {len(typo_tokens)} typo tokens, {len(styles_raw)} styles"
    )

    reg = build_emitted_registry(primitives, theme_colors, sizing, typo_tokens)
    walk = build_walk_map(tokens_dir)
    print(f"  Registry: {len(reg)} entries  |  WALK map: {len(walk)} entries")

    OUT_DIR.mkdir(parents=True, exist_ok=True)
    print("Emitting Kotlin …")

    (OUT_DIR / "Primitives.kt").write_text(emit_primitives(primitives), encoding="utf-8")
    print("  ✓ Primitives.kt")

    (OUT_DIR / "Colors.kt").write_text(emit_colors(theme_colors, reg, walk), encoding="utf-8")
    print("  ✓ Colors.kt")

    (OUT_DIR / "Sizing.kt").write_text(emit_sizing(sizing, reg, walk), encoding="utf-8")
    print("  ✓ Sizing.kt")

    (OUT_DIR / "TypographyTokens.kt").write_text(
        emit_typography_tokens(typo_tokens, reg, walk), encoding="utf-8"
    )
    print("  ✓ TypographyTokens.kt")

    (OUT_DIR / "Typography.kt").write_text(
        emit_typography(styles_raw, reg, walk), encoding="utf-8"
    )
    print("  ✓ Typography.kt")

    print(f"\n✓ Done. Output → {OUT_DIR}")


if __name__ == "__main__":
    main()
