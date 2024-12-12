package au.com.alfie.ecomm.feature.pdp.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed interface SizeSectionUI {

    @Stable
    data class SizeSelector(
        val sizes: ImmutableList<SizeUI>,
        val selectedSize: SizeUI? = null
    ) : SizeSectionUI

    @Stable
    data class SizeModalPicker(
        val sizes: ImmutableList<SizeUI>,
        val selectedSize: SizeUI? = null
    ) : SizeSectionUI

    data object SingleSize : SizeSectionUI

    @Stable
    data class SizeOnly(val sizeUI: SizeUI) : SizeSectionUI

    data object NoSize : SizeSectionUI
}
