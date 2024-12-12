package au.com.alfie.ecomm.designsystem.component.price

sealed interface PriceType {

    data class Default(
        val price: String
    ) : PriceType

    data class Sale(
        val fullPrice: String,
        val salePrice: String
    ) : PriceType

    data class Range(
        val startPrice: String,
        val endPrice: String
    ) : PriceType
}
