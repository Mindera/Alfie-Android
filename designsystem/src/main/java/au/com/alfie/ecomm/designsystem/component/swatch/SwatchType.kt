package au.com.alfie.ecomm.designsystem.component.swatch

import androidx.compose.ui.graphics.Color

sealed interface SwatchType {

    val isEnabled: Boolean

    data class PlainColor(
        val color: Color,
        override val isEnabled: Boolean = true
    ) : SwatchType

    data class Image(
        val url: String,
        override val isEnabled: Boolean = true
    ) : SwatchType
}
