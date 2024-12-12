package au.com.alfie.ecomm.core.navigation.arguments

fun productDetailsNavArgs(
    id: String
): ProductDetailsNavArgs = ProductDetailsNavArgs(id = id)

data class ProductDetailsNavArgs(
    val id: String
)
