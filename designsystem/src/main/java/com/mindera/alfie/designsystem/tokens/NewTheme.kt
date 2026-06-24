package com.mindera.alfie.designsystem.tokens

/**
 * New design-token facade backed by generated token objects.
 *
 * Usage: NewTheme.color.surface.backgroundPrimary, NewTheme.typography.display.large, etc.
 *
 * Migrated call sites should import this object; legacy code continues using Theme.*
 * until migration is complete (at which point NewTheme is renamed to Theme).
 */
object NewTheme {

    val color: Colors = Colors

    val typography: Typography = Typography

    val spacing: Spacing = Spacing

    val sizing: Sizing = Sizing
}
