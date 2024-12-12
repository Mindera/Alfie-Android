package au.com.alfie.ecomm.designsystem.component.sizingbutton

import androidx.compose.runtime.Stable

@Stable
data class SizingButtonProperties(
    val text: String,
    val state: SizingButtonState
) {
    companion object {
        val EMPTY = SizingButtonProperties(
            text = "",
            state = SizingButtonState.OutOfStock
        )
    }
}
