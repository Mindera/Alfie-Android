package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.mappers.toImageUI
import au.com.alfie.ecomm.feature.wishlist.models.WishlistProductUi
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
import au.com.alfie.ecomm.repository.shared.model.Size

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
                sku = "234rtghnm"
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
            sku = "234rtghnm"
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
                sku = "234rtghnm"
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
            sku = "234rtghnm"
        )
    )
)

internal val wishListProductUi = listOf(
    WishlistProductUi(
        productCardData = ProductCardType.Medium(
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
    WishlistProductUi(
        productCardData = ProductCardType.Medium(
            brand = "Brand",
            name = "Product 2",
            price = PriceType.Default("$100"),
            image = Media.Image(
                url = "",
                alt = null
            ).toImageUI(),
            color = "",
            size = ""
        )
    )
)