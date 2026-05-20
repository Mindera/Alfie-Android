package com.mindera.alfie.feature.pdp.model

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.commons.string.StringResource

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
