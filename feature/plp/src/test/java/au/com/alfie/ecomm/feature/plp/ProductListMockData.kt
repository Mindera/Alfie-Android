package au.com.alfie.ecomm.feature.plp

import au.com.alfie.ecomm.core.ui.media.image.ImageSizeUI
import au.com.alfie.ecomm.core.ui.media.image.ImageUI
import au.com.alfie.ecomm.designsystem.component.price.PriceType
import au.com.alfie.ecomm.designsystem.component.productcard.ProductCardType
import au.com.alfie.ecomm.feature.plp.model.ProductListEntryUI
import au.com.alfie.ecomm.feature.plp.model.ProductListUI
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntryVariant
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
import au.com.alfie.ecomm.repository.shared.model.Size
import kotlinx.collections.immutable.persistentListOf

internal val products = listOf(
    ProductListEntry(
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
                ),
            )
        ),
        longDescription = "",
        attributes = listOf(),
        variants = listOf(
            ProductListEntryVariant(
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
                color = "111",
                size = Size(
                    id = "789",
                    value = "M",
                    description = "Medium",
                    scale = "Scale",
                    sizeGuide = null
                ),
                media = listOf(
                    Media.Image(
                        url = "",
                        alt = "Media"
                    )
                ),
                stock = 100
            )
        ),
        defaultVariant = ProductListEntryVariant(
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
            color = "111",
            size = Size(
                id = "789",
                value = "M",
                description = "Medium",
                scale = "Scale",
                sizeGuide = null
            ),
            media = listOf(
                Media.Image(
                    url = "",
                    alt = "Media"
                )
            ),
            stock = 100
        )
    ),
    ProductListEntry(
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
            ProductListEntryVariant(
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
                color = "111",
                size = Size(
                    id = "789",
                    value = "M",
                    description = "Medium",
                    scale = "Scale",
                    sizeGuide = null
                ),
                media = listOf(
                    Media.Image(
                        url = "",
                        alt = "Media"
                    )
                ),
                stock = 100
            )
        ),
        defaultVariant = ProductListEntryVariant(
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
            media = listOf(
                Media.Image(
                    url = "",
                    alt = null
                )
            ),
            stock = 1
        )
    )
)

internal val productListMetadata = ProductListMetadata(
    title = "Women",
    totalResults = 2,
)

internal val productsMediumUI = listOf(
    ProductListEntryUI(
        id = "123456",
        productCardData = ProductCardType.Medium(
            name = "Product name",
            brand = "Brand",
            price = PriceType.Range(
                startPrice = "$100",
                endPrice = "$200"
            ),
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "",
                    )
                ),
                alt = "Media"
            ),
            onFavoriteClick = { }
        )
    ),
    ProductListEntryUI(
        id = "654321",
        productCardData = ProductCardType.Medium(
            name = "Product 2",
            brand = "Brand",
            price = PriceType.Default(price = "$100"),
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "",
                    )
                ),
                alt = null
            ),
            onFavoriteClick = { }
        )
    )
)

internal val productsLargeUI = listOf(
    ProductListEntryUI(
        id = "123456",
        productCardData = ProductCardType.Large(
            name = "Product name",
            brand = "Brand",
            price = PriceType.Range(
                startPrice = "$100",
                endPrice = "$200"
            ),
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "",
                    )
                ),
                alt = "Media"
            ),
            onFavoriteClick = { }
        )
    ),
    ProductListEntryUI(
        id = "654321",
        productCardData = ProductCardType.Large(
            name = "Product 2",
            brand = "Brand",
            price = PriceType.Default(price = "$100"),
            image = ImageUI(
                images = persistentListOf(
                    ImageSizeUI.Custom(
                        url = "",
                    )
                ),
                alt = null
            ),
            onFavoriteClick = { }
        )
    )
)

internal val gridProductListUI = ProductListUI(
    title = "Women",
    resultCount = 2,
    isLoadingMetadata = false,
    layoutMode = ProductListLayoutMode.GRID,
    compactColumnCount = 2,
    nonCompactColumnCount = 3
)

internal val columnProductListUI = ProductListUI(
    title = "Women",
    resultCount = 2,
    isLoadingMetadata = false,
    layoutMode = ProductListLayoutMode.COLUMN,
    compactColumnCount = 1,
    nonCompactColumnCount = 2
)
