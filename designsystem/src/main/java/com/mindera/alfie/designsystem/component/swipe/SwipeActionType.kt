package com.mindera.alfie.designsystem.component.swipe

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mindera.alfie.designsystem.tokens.LocalTheme

/**
 * Visual variants of a [SwipeAction] panel, mirroring the Figma "Button (Vertical)" variants used by
 * the Bag row's hidden buttons (node `3911:82891`).
 */
enum class SwipeActionType {
    /** Transparent panel with primary content — Figma `button/terciary/background-terciary-default`. */
    Neutral,

    /** Filled destructive panel — Figma `button/destructive/background-destructive-default`. */
    Destructive
}

/** Background of the action panel. */
@Composable
fun SwipeActionType.backgroundColor(): Color {
    val button = LocalTheme.current.color.button
    return when (this) {
        SwipeActionType.Neutral -> button.terciaryBackgroundTerciaryDefault
        SwipeActionType.Destructive -> button.destructiveBackgroundDestructiveDefault
    }
}

/** Icon and label colour of the action panel. */
@Composable
fun SwipeActionType.contentColor(): Color {
    val button = LocalTheme.current.color.button
    return when (this) {
        SwipeActionType.Neutral -> button.terciaryContentTerciaryDefault
        SwipeActionType.Destructive -> button.destructiveContentDestructiveDefault
    }
}
