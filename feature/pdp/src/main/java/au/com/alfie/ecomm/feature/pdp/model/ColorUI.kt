package au.com.alfie.ecomm.feature.pdp.model

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.designsystem.component.swatch.SwatchType

@Stable
internal data class ColorUI(
    val id: String,
    val type: SwatchType,
    val index: Int,
    val isSelected: Boolean = false
)
