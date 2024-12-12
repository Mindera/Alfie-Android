package au.com.alfie.ecomm.designsystem.component.button

import androidx.annotation.DrawableRes

data class IconButton(
    @DrawableRes val iconResource: Int,
    val position: IconPosition,
    val contentDescription: String? = null
)

enum class IconPosition {
    Left,
    Right
}
