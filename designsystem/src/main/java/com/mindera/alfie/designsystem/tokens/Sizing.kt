// GENERATED — do not edit. Produced by scripts/generate_tokens/generate_design_tokens.py
// Source: designsystem/src/main/assets/design_tokens (Mindera/Alfie-Mobile-Design-Tokens). Re-run on token changes.
@file:Suppress("MagicNumber", "LongMethod")
package com.mindera.alfie.designsystem.tokens

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Immutable
interface Sizing {
    val icon: SizingIcon
    val radius: SizingRadius
    val interactive: SizingInteractive
}

@Immutable
interface SizingIcon {
    val large: Dp
    val medium: Dp
    val small: Dp
    val xlarge: Dp
}

@Immutable
interface SizingRadius {
    val rounded: Shape
    val soft: Shape
    val strong: Shape
}

@Immutable
interface SizingInteractive {
    val smallPaddingLeftRight: Dp
    val smallPaddingTopBottom: Dp
}

@Immutable
class DefaultSizing(private val primitive: Primitives) : Sizing {
    override val icon = object : SizingIcon {
        override val large = primitive.spacing.spacing32
        override val medium = primitive.spacing.spacing24
        override val small = primitive.spacing.spacing16
        override val xlarge = primitive.spacing.spacing40
    }
    override val radius = object : SizingRadius {
        override val rounded = CircleShape
        override val soft = RoundedCornerShape(primitive.spacing.spacing4)
        override val strong = RoundedCornerShape(primitive.spacing.spacing16)
    }
    override val interactive = object : SizingInteractive {
        override val smallPaddingLeftRight = primitive.spacing.spacing8
        override val smallPaddingTopBottom = primitive.spacing.spacing8
    }
}
