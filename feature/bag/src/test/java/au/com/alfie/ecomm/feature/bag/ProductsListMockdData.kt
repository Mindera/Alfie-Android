package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.bag.models.BagProductUi
import au.com.alfie.ecomm.feature.mappers.toImageUI
import au.com.alfie.ecomm.repository.bag.BagProduct
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
import au.com.alfie.ecomm.repository.shared.model.Size
import kotlinx.collections.immutable.persistentListOf

internal val products = listOf(
    Product(
        id = "123456",
        name = "Product name",
        shortDescription = "Short description",
        slug = "123456-product",
        styleNumber = "123456789",
        labels = listOf("Label"),
        brand = Brand(
            id = "123",
            name = "Brand",
            slug = "brand"
        ),
        priceRange = PriceRange(
            low = Money(
                amount = 100,
                amountFormatted = "$100",
                currencyCode = "AUS"
            ),
            high = Money(
                amount = 200,
                amountFormatted = "$200",
                currencyCode = "AUS"
            )
        ),
        colors = listOf(
            Color(
                id = "111",
                name = "blue",
                swatch = Media.Image(
                    url = "",
                    alt = "Blue"
                ),
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                )
            )
        ),
        longDescription = "",
        attributes = listOf(),
        variants = listOf(
            Variant(
                price = Price(
                    amount = Money(
                        amount = 100,
                        amountFormatted = "$100",
                        currencyCode = "AUS"
                    ),
                    was = Money(
                        amount = 200,
                        amountFormatted = "$200",
                        currencyCode = "AUS"
                    )
                ),
                color = Color(
                    id = "111",
                    name = "blue",
                    swatch = Media.Image(
                        url = "",
                        alt = "Blue"
                    ),
                    media = listOf(
                        Media.Image(
                            alt = "patterson mini skirt",
                            url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                        ),
                        Media.Image(
                            alt = "patterson mini skirt",
                            url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                        )
                    )
                ),
                size = Size(
                    id = "789",
                    value = "M",
                    description = "Medium",
                    scale = "Scale",
                    sizeGuide = null
                ),
                media = Media.Image(
                    url = "",
                    alt = "Media"
                ),
                stock = 100,
                attributes = listOf(),
                sku = "variant1"
            )
        ),
        defaultVariant = Variant(
            price = Price(
                amount = Money(
                    amount = 100,
                    amountFormatted = "$100",
                    currencyCode = "AUS"
                ),
                was = Money(
                    amount = 200,
                    amountFormatted = "$200",
                    currencyCode = "AUS"
                )
            ),
            size = Size(
                id = "789",
                value = "M",
                description = "Medium",
                scale = "Scale",
                sizeGuide = null
            ),
            color = Color(
                id = "111",
                name = "blue",
                swatch = Media.Image(
                    url = "",
                    alt = "Blue"
                ),
                media = listOf(
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                    ),
                    Media.Image(
                        alt = "patterson mini skirt",
                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                    )
                )
            ),
            media = Media.Image(
                url = "",
                alt = "Media"
            ),
            stock = 100,
            attributes = listOf(),
            sku = "variant2"
        )
    ),
    Product(
        id = "654321",
        name = "Product 2",
        shortDescription = "Short description",
        slug = "654321-product",
        styleNumber = "987654321",
        labels = emptyList(),
        brand = Brand(
            id = "123",
            name = "Brand",
            slug = "brand"
        ),
        priceRange = PriceRange(
            low = Money(
                amount = 100,
                amountFormatted = "$100",
                currencyCode = "AUS"
            ),
            high = null
        ),
        colors = emptyList(),
        longDescription = "",
        attributes = listOf(),
        variants = listOf(
            Variant(
                price = Price(
                    amount = Money(
                        amount = 100,
                        amountFormatted = "$100",
                        currencyCode = "AUS"
                    ),
                    was = null
                ),
                color = Color(
                    id = "111",
                    name = "blue",
                    swatch = Media.Image(
                        url = "",
                        alt = "Blue"
                    ),
                    media = listOf(
                        Media.Image(
                            alt = "patterson mini skirt",
                            url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                        ),
                        Media.Image(
                            alt = "patterson mini skirt",
                            url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                        )
                    )
                ),
                size = Size(
                    id = "789",
                    value = "M",
                    description = "Medium",
                    scale = "Scale",
                    sizeGuide = null
                ),
                media = Media.Image(
                    url = "",
                    alt = "Media"
                ),
                stock = 100,
                attributes = listOf(),
                sku = "variant11"
            )
        ),
        defaultVariant = Variant(
            price = Price(
                amount = Money(
                    amount = 100,
                    amountFormatted = "$100",
                    currencyCode = "AUS"
                ),
                was = null
            ),
            color = null,
            size = null,
            media = Media.Image(
                url = "",
                alt = null
            ),
            stock = 1,
            attributes = listOf(),
            sku = "variant12"
        )
    )
)

internal val bagProducts = listOf(
    BagProduct(productId = "123456", variantSku = "variant1"),
    BagProduct(productId = "654321", variantSku = "variant11")
)

internal val bagProductUi = persistentListOf(
    BagProductUi(
        id = "123456",
        productCardData = ProductCardType.XSmall(
            brand = "Brand",
            name = "Product name",
            price = PriceType.Range(
                startPrice = "$100",
                endPrice = "$200"
            ),
            image = Media.Image(
                url = "",
                alt = "Media"
            ).toImageUI(),
            color = "blue",
            size = "M"
        )
    ),
    BagProductUi(
        id = "654321",
        productCardData = ProductCardType.XSmall(
            brand = "Brand",
            name = "Product 2",
            price = PriceType.Default(
                price = "$100"
            ),
            image = Media.Image(
                url = "",
                alt = "Media"
            ).toImageUI(),
            color = "blue",
            size = "M"
        )
    )
)