# Alfie Android — Localization

This document covers string resource management, naming conventions, and localization patterns for the Alfie Android application.

---

## Localization

### String Resources

- **Location**: Module-specific `res/values/strings.xml`
- **Main strings**: `designsystem/src/main/res/values/strings.xml`
- **Naming convention**: `feature_screen_element` (e.g., `plp_filter_title`)

### Usage

```xml
<string name="home_greeting">Welcome, %1$s!</string>
```
```kotlin
Text(text = stringResource(R.string.home_greeting, userName))
```

**Important**: Never hardcode user-facing strings. Always use string resources. See `localization-specialist` agent for full workflow.

### Plurals Pattern

```xml
<plurals name="bag_items_count">
    <item quantity="one">%d item</item>
    <item quantity="other">%d items</item>
</plurals>
```

Usage:
```kotlin
stringResource(R.string.home_header_title)
pluralStringResource(R.plurals.bag_items_count, count, count)
```

### Naming Conventions

| Pattern | Example |
|---------|---------|
| `feature_screen_element` | `plp_filter_title` |
| `feature_screen_element` | `home_header_title` |
| `feature_screen_element` | `bag_items_count` |

### Key Rules

| ✅ Do | ❌ Don't |
|---|---|
| Use `feature_screen_element` naming | Use vague names like `text1` |
| Use `stringResource()` in Composables | Hardcode strings in Kotlin code |
| Add plurals for countable items | Use string concatenation for plurals |
| Place strings in the owning module | Place all strings in the app module |
