package au.com.alfie.ecomm.designsystem.component.price

import androidx.compose.runtime.Stable

@Stable
sealed interface PriceType {

    @Stable
    data class Default(
        val price: String
    ) : PriceType

    @Stable
    data class Sale(
        val fullPrice: String,
        val salePrice: String
    ) : PriceType

    @Stable
    data class Range(
        val startPrice: String,
        val endPrice: String
    ) : PriceType
}
