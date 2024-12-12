package au.com.alfie.ecomm.feature.pdp.model

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.commons.string.StringResource

@Stable
data class ProductDetailsShareInfo(
    val name: String,
    val content: StringResource
) {
    companion object {
        val EMPTY = ProductDetailsShareInfo(
            name = "",
            content = StringResource.EMPTY
        )
    }
}
