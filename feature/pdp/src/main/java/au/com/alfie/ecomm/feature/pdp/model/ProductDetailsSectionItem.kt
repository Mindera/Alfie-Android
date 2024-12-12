package au.com.alfie.ecomm.feature.pdp.model

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.commons.string.StringResource

@Stable
internal data class ProductDetailsSectionItem(
    val title: StringResource,
    val url: String
) {
    companion object {
        val EMPTY = ProductDetailsSectionItem(
            title = StringResource.EMPTY,
            url = ""
        )
    }
}
