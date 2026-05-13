package au.com.alfie.ecomm.designsystem.component.productcard.size

import androidx.compose.ui.unit.Dp

sealed interface VerticalProductCardSize {
    /** Card fills the full available parent width. */
    data object Large : VerticalProductCardSize

    /**
     * Card renders at an explicit [cardWidth]. Callers are responsible for computing the correct
     * value from the available layout width (e.g. via [androidx.compose.foundation.layout.BoxWithConstraints]).
     */
    data class Medium(val cardWidth: Dp) : VerticalProductCardSize
}
