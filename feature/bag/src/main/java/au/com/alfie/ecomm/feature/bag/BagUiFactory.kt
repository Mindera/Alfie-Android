package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import kotlinx.coroutines.withContext
import javax.inject.Inject
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.feature.mappers.mapPrice
import au.com.alfie.ecomm.feature.mappers.mapImage

class BagUiFactory @Inject constructor(
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke(
        products: List<Product>,
    ): List<BagProductUi> = withContext(dispatcher.default()) {
        products.map {
            BagProductUi(
                productCardData = mapProductCardData(product = it)
            )
        }
    }

    private fun mapProductCardData(
        product: Product,
    ) = ProductCardType.XSmall(
        brand = product.brand.name,
        name = product.name,
        price = mapPrice(
            priceRange = product.priceRange,
            price = product.defaultVariant.price
        ),
        image = mapImage(product.defaultVariant.media),
        color = product.defaultVariant.color?.name ?: "",
        size = product.defaultVariant.size?.value ?: ""
    )
}