#!/usr/bin/env python3
"""
Design-token code generator: reads W3C DTCG JSON from
designsystem/src/main/assets/design_tokens/ and emits type-safe Kotlin
into designsystem/src/main/java/com/mindera/alfie/designsystem/tokens/.

Run: python3 scripts/generate_tokens/generate_design_tokens.py
Re-run whenever design-token JSON files are updated.
"""
import json
import re
import sys
import warnings
from pathlib import Path

# ---------------------------------------------------------------------------
# Paths (relative to repo root, resolved from this script's location)
# ---------------------------------------------------------------------------
REPO_ROOT = Path(__file__).resolve().parent.parent.parent
TOKENS_DIR = REPO_ROOT / "designsystem" / "src" / "main" / "assets" / "design_tokens"
OUT_DIR = (
    REPO_ROOT
    / "designsystem"
    / "src"
    / "main"
    / "java"
    / "com"
    / "mindera"
    / "alfie"
    / "designsystem"
    / "tokens"
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
MAX_DEPTH = 10  # chains through strikethrough/bold indirections can reach 7 levels

# ---------------------------------------------------------------------------
# Loading
# ---------------------------------------------------------------------------

def _load(path: Path) -> dict:
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def _collect_files(manifest: dict) -> list[str]:
    files: list[str] = []
    seen: set[str] = set()

    def _add(name: str) -> None:
        if name not in seen:
            seen.add(name)
            files.append(name)

    collections = manifest.get("collections", {})
    for coll_name, coll in collections.items():
        if coll_name == ".documentation":
            continue
        modes = coll.get("modes", {})
        for mode_name, file_list in modes.items():
            for f in file_list:
                _add(f)

    for file_list in manifest.get("styles", {}).values():
        for f in file_list:
            _add(f)

    return files


# Android + Small-screen priority: load these last so their values win
_PRIORITY_ORDER = [
    ".primitives.alfie-theme.tokens.json",
    "theme.alfie-theme.tokens.json",
    "typography.alfie-theme.tokens.json",
    "sizing.alfie-theme.tokens.json",
    # platform/screen-size non-priority loaded before priority ones
]
_ANDROID_FILES = {"system.android.tokens.json"}
_SMALL_FILES = {"screen-size.small-(s).tokens.json"}
_STYLES_FILE = "typography.styles.tokens.json"


def load_token_map(tokens_dir: Path) -> tuple[dict[str, dict], dict[str, dict]]:
    """Return (token_map, primitives_map).

    token_map: flat {token_name: {$type, $value, _file}}, Android/Small last-wins.
    primitives_map: same but only from .primitives.alfie-theme.tokens.json (concrete values).
    """
    manifest = _load(tokens_dir / "manifest.json")
    all_files = _collect_files(manifest)

    def sort_key(f: str) -> int:
        if f in _SMALL_FILES:
            return 4
        if f in _ANDROID_FILES:
            return 3
        if f == _STYLES_FILE:
            return 5
        if f.startswith("screen-size.") or f.startswith("system."):
            return 2  # other platform/screen files — low priority
        return 1  # primitives, theme, typography.alfie-theme, sizing

    ordered = sorted(all_files, key=sort_key)
    token_map: dict[str, dict] = {}
    primitives_map: dict[str, dict] = {}
    PRIMITIVES_FILE = ".primitives.alfie-theme.tokens.json"

    for fname in ordered:
        path = tokens_dir / fname
        if not path.exists():
            raise FileNotFoundError(f"manifest references missing file: {fname}")
        raw = _load(path)
        for name, obj in raw.items():
            if name.startswith(DOC_PREFIX) or name.startswith("$"):
                continue
            if isinstance(obj, dict) and "$value" in obj:
                entry = {"$type": obj.get("$type"), "$value": obj["$value"], "_file": fname}
                token_map[name] = entry
                if fname == PRIMITIVES_FILE:
                    primitives_map[name] = entry

    return token_map, primitives_map


# ---------------------------------------------------------------------------
# Ref extraction & resolution
# ---------------------------------------------------------------------------

def _extract_refs(value) -> list[str]:
    refs: list[str] = []

    def walk(v):
        if isinstance(v, str):
            m = REF_RE.match(v)
            if m:
                refs.append(m.group(1))
        elif isinstance(v, list):
            for item in v:
                walk(item)
        elif isinstance(v, dict):
            for k, child in v.items():
                walk(child)

    walk(value)
    return refs


def _resolve_value(value, token_map: dict, primitives_map: dict,
                   cycle_allow_set: set, broken_allow_set: set,
                   token_name: str, file_name: str, _chain=None):
    """Recursively resolve {ref} aliases in value. Returns the concrete value."""
    if _chain is None:
        _chain = frozenset()

    if isinstance(value, str):
        m = REF_RE.match(value)
        if m:
            target = m.group(1)
            if target not in token_map:
                if target in broken_allow_set:
                    return None  # allowlisted broken ref
                raise RuntimeError(
                    f"Broken reference: {file_name}::{token_name} → {{{target}}} (missing, not allow-listed)"
                )
            target_entry = token_map[target]
            fkey = f"{target_entry['_file']}::{target}"
            if fkey in cycle_allow_set or target in _chain:
                # Cycle — resolve from the primitives-only map (pre-override values)
                warnings.warn(f"Allow-listed cycle involving '{target}', resolving from primitives", stacklevel=3)
                prim = primitives_map.get(target)
                if prim is not None and not (isinstance(prim["$value"], str) and REF_RE.match(prim["$value"])):
                    return prim["$value"]
                # primitives entry also a ref — resolve it without cycling
                return _resolve_value(
                    prim["$value"] if prim else None, token_map, primitives_map,
                    cycle_allow_set, broken_allow_set, target, ".primitives", _chain | {target}
                ) if prim else None
            new_chain = _chain | {target}
            if len(new_chain) > MAX_DEPTH:
                raise RuntimeError(f"Max alias depth ({MAX_DEPTH}) exceeded resolving '{token_name}' in {file_name}")
            return _resolve_value(
                target_entry["$value"], token_map, primitives_map,
                cycle_allow_set, broken_allow_set, target, target_entry["_file"], new_chain
            )
        return value

    if isinstance(value, list):
        return [
            _resolve_value(item, token_map, primitives_map, cycle_allow_set, broken_allow_set, token_name, file_name, _chain)
            for item in value
        ]

    if isinstance(value, dict):
        return {
            k: _resolve_value(v, token_map, primitives_map, cycle_allow_set, broken_allow_set, token_name, file_name, _chain)
            for k, v in value.items()
        }

    return value


def resolve_all(token_map: dict, primitives_map: dict, cycle_allow_set: set, broken_allow_set: set) -> dict[str, dict]:
    """Return token_map with all $values fully resolved."""
    resolved: dict[str, dict] = {}
    errors: list[str] = []

    for name, entry in token_map.items():
        try:
            val = _resolve_value(
                entry["$value"], token_map, primitives_map,
                cycle_allow_set, broken_allow_set, name, entry["_file"]
            )
            resolved[name] = {**entry, "$value": val}
        except RuntimeError as exc:
            errors.append(str(exc))

    if errors:
        print("\n✗ Resolution failed with errors:", file=sys.stderr)
        for e in errors:
            print(f"    {e}", file=sys.stderr)
        sys.exit(1)

    return resolved


# ---------------------------------------------------------------------------
# Validation (port of validate.ts)
# ---------------------------------------------------------------------------

def validate(token_map: dict, cycle_allow_set: set, broken_allow_set: set) -> None:
    by_name: dict[str, list[tuple[str, str]]] = {}
    for name, entry in token_map.items():
        by_name.setdefault(name, []).append((entry["_file"], name))

    cycle_nodes: set[str] = set()
    broken_refs: list[dict] = []
    broken_ref_set: set[str] = set()

    for start_name, start_entry in token_map.items():
        refs = _extract_refs(start_entry["$value"])
        if not refs:
            continue
        start_key = f"{start_entry['_file']}::{start_name}"
        visited: set[str] = set()
        queue = [(start_entry["_file"], start_name)]
        found_cycle = False

        while queue and not found_cycle:
            cur_file, cur_name = queue.pop(0)
            cur_entry = token_map.get(cur_name)
            if cur_entry is None:
                continue
            for ref in _extract_refs(cur_entry["$value"]):
                if ref not in token_map:
                    key = f"{cur_file}::{cur_name}::{ref}"
                    if key not in broken_ref_set:
                        broken_ref_set.add(key)
                        broken_refs.append({"file": cur_file, "token": cur_name, "missing": ref})
                    continue
                t_entry = token_map[ref]
                t_key = f"{t_entry['_file']}::{ref}"
                if t_key == start_key:
                    found_cycle = True
                    break
                if t_key not in visited:
                    visited.add(t_key)
                    queue.append((t_entry["_file"], ref))
            if found_cycle:
                break

        if found_cycle:
            cycle_nodes.add(start_key)

    errors: list[str] = []

    for cn in cycle_nodes:
        if cn not in cycle_allow_set:
            errors.append(f"unexpected cycle: {cn}")
    # Note: stale cycle allowlist entries are expected here because we only load
    # Android/Small-screen tokens, not all platform/screen-size variants.

    seen_missing: set[str] = set()
    for br in broken_refs:
        seen_missing.add(br["missing"])
        if br["missing"] not in broken_allow_set:
            errors.append(f"broken reference: {br['file']}::{br['token']} → {{{br['missing']}}} (missing, not allow-listed)")
    for allowed in broken_allow_set:
        if allowed not in seen_missing:
            errors.append(f"stale broken-ref allow-list entry: {allowed} (no broken refs to this target)")

    matched_cycles = len(cycle_nodes & cycle_allow_set)
    if matched_cycles > 0:
        print(f"⚠ {matched_cycles} allow-listed cycle(s) (Figma plugin re-export artifact)")
    if broken_refs and not errors:
        print(f"⚠ {len(broken_refs)} allow-listed broken reference(s)")

    if errors:
        print(f"\n✗ Validation failed with {len(errors)} error(s):", file=sys.stderr)
        for e in errors:
            print(f"    {e}", file=sys.stderr)
        sys.exit(1)

    print(f"✓ Validation passed: {len(token_map)} tokens, {matched_cycles} allow-listed cycle(s), {len(broken_refs)} allow-listed broken ref(s).")


# ---------------------------------------------------------------------------
# Value conversion
# ---------------------------------------------------------------------------

def _to8(f: float) -> int:
    return max(0, min(255, round(f * 255)))


def color_to_kotlin(value: dict) -> str:
    """Convert resolved color dict → Color(0xAARRGGBB)."""
    comps = value["components"]
    alpha = value.get("alpha", 1.0)
    r, g, b = comps[0], comps[1], comps[2]
    aa = _to8(alpha)
    rr = _to8(r)
    gg = _to8(g)
    bb = _to8(b)
    return f"Color(0x{aa:02X}{rr:02X}{gg:02X}{bb:02X})"


def dim_to_dp(value) -> str:
    """Resolved dimension → Dp literal."""
    if isinstance(value, dict):
        v = value["value"]
    else:
        v = float(value)
    iv = int(v)
    return f"{iv}.dp" if iv == v else f"{v}.dp"


def dim_to_sp(value) -> str:
    """Resolved dimension → Sp literal."""
    if isinstance(value, dict):
        v = value["value"]
    else:
        v = float(value)
    iv = int(v)
    if v < 0:
        return f"({v}).sp" if iv != v else f"({iv}).sp"
    return f"{iv}.sp" if iv == v else f"{v}.sp"


FONT_WEIGHT_MAP = {
    "Regular": "FontWeight.Normal",
    "Medium": "FontWeight.W500",
    "SemiBold": "FontWeight.W600",
    "Bold": "FontWeight.Bold",
}


def font_weight_to_kotlin(weight_str: str) -> str:
    return FONT_WEIGHT_MAP.get(weight_str, "FontWeight.Normal")


# ---------------------------------------------------------------------------
# Name helpers
# ---------------------------------------------------------------------------

def _to_camel(segment: str) -> str:
    """Convert hyphenated segment to camelCase, e.g. 'x-small' → 'xSmall'."""
    parts = segment.split("-")
    return parts[0] + "".join(p.capitalize() for p in parts[1:])


def _to_pascal(s: str) -> str:
    """Capitalize the first letter of a camelCase string → PascalCase."""
    return s[0].upper() + s[1:] if s else s


def semantic_color_group_and_member(name: str) -> tuple[str, str]:
    """Split semantic color token name into (group, member) for nested Kotlin."""
    parts = name.split("-", 1)
    group = _to_camel(parts[0])
    member = _to_camel(parts[1]) if len(parts) > 1 else "value"
    return group, member


def primitive_color_group_and_member(name: str) -> tuple[str, str]:
    """
    'colours-neutrals-900' → ('neutrals', 'n900')
    'colours-semantic-error-800' → ('semanticError', 'e800')
    'colours-transparent-transparent' → ('transparent', 'transparent')
    """
    # strip 'colours-' prefix
    rest = name[len("colours-"):]
    parts = rest.split("-")
    if len(parts) == 2:
        group = _to_camel(parts[0])
        member = parts[0][0] + parts[1]  # e.g. 'n900', 't0'
    elif len(parts) == 3:
        group = _to_camel(f"{parts[0]}-{parts[1]}")
        member = parts[1][0] + parts[2]  # e.g. 'e800'
    else:
        group = _to_camel("-".join(parts[:-1]))
        member = parts[-1]
    # handle 'transparent-transparent'
    if parts[-1] == parts[0]:
        member = parts[-1]
    return group, member


def typography_group_and_member(style_name: str) -> tuple[str, str]:
    """
    'display-large' → ('display', 'large')
    'heading-x-small' → ('heading', 'xSmall')
    'body-medium-strikethrough' → ('body', 'mediumStrikethrough')
    'label-small-bold' → ('label', 'smallBold')
    """
    parts = style_name.split("-", 1)
    group = parts[0]
    member = _to_camel(parts[1]) if len(parts) > 1 else "default"
    return group, member


def spacing_member(name: str) -> str:
    """'spacing-spacing-16' → 'spacing16'"""
    # drop 'spacing-spacing-' prefix
    suffix = name.replace("spacing-spacing-", "")
    return f"spacing{suffix}"


def icon_member(name: str) -> str:
    """'icons-icon-small' → 'small'"""
    return name.replace("icons-icon-", "")


def radius_member(name: str) -> str:
    """'radius-soft' → 'soft', 'radius-rounded' → 'rounded'"""
    return name.replace("radius-", "")


def interactive_member(name: str) -> str:
    """'interactive-small-padding-top-bottom' → 'smallPaddingTopBottom'"""
    rest = name.replace("interactive-", "")
    return _to_camel(rest)


# ---------------------------------------------------------------------------
# Kotlin emitters
# ---------------------------------------------------------------------------

def find_primitive_ref(token_name: str, token_map: dict):
    """Walk raw token_map ref chain from token_name; return first colours-* name found, or None."""
    visited = set()
    current = token_name
    while True:
        if current in visited:
            return None
        visited.add(current)
        entry = token_map.get(current)
        if entry is None:
            return None
        val = entry["$value"]
        if not isinstance(val, str):
            return None
        m = REF_RE.match(val)
        if not m:
            return None
        target = m.group(1)
        if target.startswith("colours-"):
            return target
        current = target


def emit_colors(resolved: dict[str, dict], token_map: dict) -> str:
    """Emit Colors.kt with internal primitives + public semantics."""
    # Collect primitive colors
    primitives: dict[str, dict] = {
        name: e for name, e in resolved.items()
        if name.startswith("colours-") and e.get("$type") == "color"
    }
    # Collect semantic colors (surface, content, link, button, border)
    semantic_prefixes = ("surface-", "content-", "link-", "button-", "border-")
    semantics: dict[str, dict] = {
        name: e for name, e in resolved.items()
        if any(name.startswith(p) for p in semantic_prefixes) and e.get("$type") == "color"
    }

    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\", \"ObjectPropertyNaming\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.ui.graphics.Color\n",
    ]

    lines.append("\nobject Colors {\n")

    # --- internal Primitives nested inside Colors ---
    lines.append("    internal object Primitives {\n")

    # Group primitives by group name
    prim_groups: dict[str, list[tuple[str, str, str]]] = {}  # group → [(member, kotlin_val, original_name)]
    for name, entry in sorted(primitives.items()):
        val = entry["$value"]
        if not isinstance(val, dict) or "components" not in val:
            continue
        group, member = primitive_color_group_and_member(name)
        prim_groups.setdefault(group, []).append((member, color_to_kotlin(val), name))

    for group, members in sorted(prim_groups.items()):
        lines.append(f"        object {_to_pascal(group)} {{\n")
        for member, kotlin_val, orig in sorted(members):
            lines.append(f"            val {member} = {kotlin_val}\n")
        lines.append("        }\n")

    lines.append("    }\n\n")

    # --- public semantic objects ---
    sem_groups: dict[str, list[tuple[str, str]]] = {}
    for name, entry in sorted(semantics.items()):
        val = entry["$value"]
        if not isinstance(val, dict) or "components" not in val:
            continue
        group, member = semantic_color_group_and_member(name)
        prim_name = find_primitive_ref(name, token_map)
        if prim_name is not None:
            prim_group, prim_member = primitive_color_group_and_member(prim_name)
            kotlin_val = f"Primitives.{_to_pascal(prim_group)}.{prim_member}"
        else:
            kotlin_val = color_to_kotlin(val)
        sem_groups.setdefault(group, []).append((member, kotlin_val))

    for group, members in sorted(sem_groups.items()):
        lines.append(f"    object {_to_pascal(group)} {{\n")
        for member, kotlin_val in sorted(members):
            lines.append(f"        val {member} = {kotlin_val}\n")
        lines.append("    }\n")

    lines.append("}\n")

    return "".join(lines)


def emit_typography(resolved: dict[str, dict], styles_raw: dict) -> str:
    """Emit Typography.kt with 15 TextStyle entries."""
    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\", \"LongMethod\")\n",
        f"package {PACKAGE}\n",
        "import androidx.compose.ui.text.PlatformTextStyle\n",
        "import androidx.compose.ui.text.TextStyle\n",
        "import androidx.compose.ui.text.font.FontWeight\n",
        "import androidx.compose.ui.unit.sp\n",
    ]

    # Map resolved font-family names to FontFamilies.kt vals (same package — no import needed).
    FONT_FAMILY_KT = {
        "Libre Baskerville": "FontFamilyBrand",
    }

    STYLE_NAMES = [
        "display-large", "display-medium", "display-small",
        "heading-large", "heading-medium", "heading-small", "heading-x-small",
        "body-large", "body-medium", "body-medium-strikethrough", "body-small",
        "link-medium", "link-small",
        "label-small", "label-small-bold",
    ]

    # group → [(member, TextStyle_args)]
    groups: dict[str, list[tuple[str, str]]] = {}
    warned_cycles: set[str] = set()

    for style_name in STYLE_NAMES:
        raw = styles_raw.get(style_name)
        if raw is None:
            print(f"  WARNING: typography style '{style_name}' not found in typography.styles.tokens.json", file=sys.stderr)
            continue

        val = raw["$value"]
        inline_weight = val.get("fontWeight", "Regular")

        # Resolve each composite field from resolved token_map
        def resolve_field(field_ref: str, as_sp: bool = False) -> str:
            if not isinstance(field_ref, str):
                # inline value (some doc tokens have inline objects)
                if isinstance(field_ref, dict):
                    return dim_to_sp(field_ref) if as_sp else dim_to_dp(field_ref)
                return str(field_ref)
            m = REF_RE.match(field_ref)
            if not m:
                return f'"{field_ref}"'
            ref_name = m.group(1)
            entry = resolved.get(ref_name)
            if entry is None:
                return "null  // broken ref"
            resolved_val = entry["$value"]
            if as_sp:
                return dim_to_sp(resolved_val)
            if isinstance(resolved_val, str):
                return f'"{resolved_val}"'  # fontFamily
            return dim_to_dp(resolved_val)

        font_family_ref = val.get("fontFamily", "")
        font_size_ref = val.get("fontSize", "")
        line_height_ref = val.get("lineHeight", "")
        letter_spacing_ref = val.get("letterSpacing", "")

        # Resolve font family → FontFamilies.kt val name
        ff_resolved = ""
        if isinstance(font_family_ref, str):
            m = REF_RE.match(font_family_ref)
            if m:
                ref_name = m.group(1)
                entry = resolved.get(ref_name)
                if entry:
                    ff_resolved = entry["$value"]
                    if not isinstance(ff_resolved, str) or REF_RE.match(ff_resolved):
                        ff_resolved = ""

        ff_kt = FONT_FAMILY_KT.get(ff_resolved, "FontFamilySystem")

        font_size_kt = resolve_field(font_size_ref, as_sp=True)
        line_height_kt = resolve_field(line_height_ref, as_sp=True)
        letter_spacing_kt = resolve_field(letter_spacing_ref, as_sp=True)
        weight_kt = font_weight_to_kotlin(inline_weight)

        style_args = (
            f"            TextStyle(\n"
            f"                platformStyle = PlatformTextStyle(includeFontPadding = false),\n"
            f"                fontFamily = {ff_kt},\n"
            f"                fontWeight = {weight_kt},\n"
            f"                fontSize = {font_size_kt},\n"
            f"                lineHeight = {line_height_kt},\n"
            f"                letterSpacing = {letter_spacing_kt},\n"
            f"            )"
        )

        group, member = typography_group_and_member(style_name)
        groups.setdefault(group, []).append((member, style_args))

    group_order = ["display", "heading", "body", "link", "label"]
    lines.append("\nobject Typography {\n")

    for g in group_order:
        members = groups.get(g, [])
        if not members:
            continue
        lines.append(f"    object {_to_pascal(g)} {{\n")
        for member, style_args in members:
            lines.append(f"        val {member}: TextStyle =\n")
            lines.append(f"{style_args}\n")
        lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


def emit_spacing(resolved: dict[str, dict]) -> str:
    """Emit GeneratedSpacing.kt."""
    spacings = {
        name: e for name, e in resolved.items()
        if name.startswith("spacing-spacing-") and e.get("$type") == "dimension"
    }

    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.ui.unit.dp\n",
        "\nobject Spacing {\n",
    ]

    def sort_key(name: str) -> float:
        try:
            return float(name.split("-")[-1])
        except ValueError:
            return 9999.0

    for name in sorted(spacings, key=sort_key):
        entry = spacings[name]
        val = entry["$value"]
        member = spacing_member(name)
        lines.append(f"    val {member} = {dim_to_dp(val)}\n")

    lines.append("}\n")
    return "".join(lines)


def emit_sizing(resolved: dict[str, dict]) -> str:
    """Emit GeneratedSizing.kt with icon sizes, radii, interactive paddings."""
    icons = {n: e for n, e in resolved.items() if n.startswith("icons-icon-") and e.get("$type") == "dimension"}
    radii = {n: e for n, e in resolved.items() if n.startswith("radius-") and e.get("$type") == "dimension"}
    interactive = {n: e for n, e in resolved.items() if n.startswith("interactive-") and e.get("$type") == "dimension"}

    lines = [
        GEN_HEADER,
        "@file:Suppress(\"MagicNumber\")\n",
        f"package {PACKAGE}\n",
        "\nimport androidx.compose.foundation.shape.CircleShape\n",
        "import androidx.compose.foundation.shape.RoundedCornerShape\n",
        "import androidx.compose.ui.unit.dp\n",
        "\nobject Sizing {\n",
    ]

    # Icons
    lines.append("    object Icon {\n")
    for name in sorted(icons):
        entry = icons[name]
        val = entry["$value"]
        member = icon_member(name)
        lines.append(f"        val {member} = {dim_to_dp(val)}\n")
    lines.append("    }\n\n")

    # Radii — emit as both Dp and RoundedCornerShape
    lines.append("    object Radius {\n")
    for name in sorted(radii):
        entry = radii[name]
        val = entry["$value"]
        member = radius_member(name)
        v_num = val["value"] if isinstance(val, dict) else val
        if v_num >= 1000:
            lines.append(f"        val {member} = CircleShape\n")
        else:
            lines.append(f"        val {member}Dp = {dim_to_dp(val)}\n")
            lines.append(f"        val {member} = RoundedCornerShape({dim_to_dp(val)})\n")
    lines.append("    }\n")

    if interactive:
        lines.append("\n    object Interactive {\n")
        for name in sorted(interactive):
            entry = interactive[name]
            val = entry["$value"]
            member = interactive_member(name)
            lines.append(f"        val {member} = {dim_to_dp(val)}\n")
        lines.append("    }\n")

    lines.append("}\n")
    return "".join(lines)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main() -> None:
    tokens_dir = TOKENS_DIR

    if not tokens_dir.exists():
        print(f"✗ Tokens directory not found: {tokens_dir}", file=sys.stderr)
        sys.exit(1)

    print(f"Loading tokens from {tokens_dir} …")

    # Load allowlists
    cycle_allow_raw = _load(tokens_dir / ".cycle-allowlist.json")
    cycle_allow_set = {f"{e['file']}::{e['token']}" for e in cycle_allow_raw["edges"]}

    broken_allow_raw = _load(tokens_dir / ".broken-ref-allowlist.json")
    broken_allow_set = set(broken_allow_raw["missingTargets"])

    token_map, primitives_map = load_token_map(tokens_dir)
    print(f"  Loaded {len(token_map)} tokens")

    print("Validating references …")
    validate(token_map, cycle_allow_set, broken_allow_set)

    print("Resolving aliases …")
    resolved = resolve_all(token_map, primitives_map, cycle_allow_set, broken_allow_set)
    print(f"  Resolved {len(resolved)} tokens")

    # Load raw typography styles for composite fontWeight (inline string)
    styles_raw_full = _load(tokens_dir / "typography.styles.tokens.json")
    styles_raw = {
        name: obj for name, obj in styles_raw_full.items()
        if not name.startswith(DOC_PREFIX)
    }

    OUT_DIR.mkdir(parents=True, exist_ok=True)

    print("Emitting Kotlin …")

    (OUT_DIR / "Colors.kt").write_text(emit_colors(resolved, token_map), encoding="utf-8")
    print("  ✓ Colors.kt")

    (OUT_DIR / "Typography.kt").write_text(
        emit_typography(resolved, styles_raw), encoding="utf-8"
    )
    print("  ✓ Typography.kt")

    (OUT_DIR / "Spacing.kt").write_text(emit_spacing(resolved), encoding="utf-8")
    print("  ✓ Spacing.kt")

    (OUT_DIR / "Sizing.kt").write_text(emit_sizing(resolved), encoding="utf-8")
    print("  ✓ Sizing.kt")

    print(f"\n✓ Done. Output → {OUT_DIR}")


if __name__ == "__main__":
    main()
