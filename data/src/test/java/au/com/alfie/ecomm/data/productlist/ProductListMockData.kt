package au.com.alfie.ecomm.data.productlist

import au.com.alfie.ecomm.graphql.ProductListingQuery
import au.com.alfie.ecomm.graphql.fragment.BrandInfo
import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.MoneyInfo
import au.com.alfie.ecomm.graphql.fragment.PriceInfo
import au.com.alfie.ecomm.graphql.fragment.PriceRangeInfo
import au.com.alfie.ecomm.graphql.fragment.SizeContainer
import au.com.alfie.ecomm.graphql.fragment.SizeInfo
import au.com.alfie.ecomm.graphql.type.MediaContentType
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.PriceRange
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntryVariant
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
import au.com.alfie.ecomm.repository.shared.model.Pagination
import au.com.alfie.ecomm.repository.shared.model.Size

internal val productListData = ProductListingQuery.Data(
    productListing = ProductListingQuery.ProductListing(
        pagination = ProductListingQuery.Pagination(
            limit = 15,
            nextPage = null,
            offset = 0,
            page = 1,
            pages = 1,
            previousPage = null,
            total = 2
        ),
        products = listOf(
            ProductListingQuery.Product(
                id = "123456",
                name = "Product name",
                shortDescription = "Short description",
                slug = "123456-product",
                styleNumber = "123456789",
                labels = listOf("Label"),
                brand = ProductListingQuery.Brand(
                    __typename = "Brand",
                    brandInfo = BrandInfo(
                        id = "123",
                        name = "Brand",
                        slug = "brand"
                    )

                ),
                priceRange = ProductListingQuery.PriceRange(
                    __typename = "PriceRange",
                    priceRangeInfo = PriceRangeInfo(
                        low = PriceRangeInfo.Low(
                            __typename = "Low",
                            moneyInfo = MoneyInfo(
                                amount = 100,
                                amountFormatted = "$100",
                                currencyCode = "AUS"
                            )
                        ),
                        high = PriceRangeInfo.High(
                            __typename = "High",
                            moneyInfo = MoneyInfo(
                                amount = 200,
                                amountFormatted = "$200",
                                currencyCode = "AUS"
                            )
                        )
                    )

                ),
                colours = listOf(
                    ProductListingQuery.Colour(
                        __typename = "Colour",
                        colorInfo = ColorInfo(
                            id = "111",
                            name = "blue",
                            swatch = ColorInfo.Swatch(
                                __typename = "Swatch",
                                imageInfo = ImageInfo(
                                    url = "",
                                    width = 50,
                                    height = 50,
                                    alt = "Blue",
                                    mediaContentType = MediaContentType.IMAGE
                                )
                            )
                        )
                    )
                ),
                sizes = listOf(
                    ProductListingQuery.Size(
                        __typename = "Size",
                        sizeContainer = SizeContainer(
                            __typename = "SizeContainer",
                            sizeGuide = null,
                            sizeInfo = SizeInfo(
                                id = "789",
                                value = "M",
                                description = "Medium",
                                scale = "Scale"
                            )
                        )
                    )
                ),
                defaultVariant = ProductListingQuery.DefaultVariant(
                    price = ProductListingQuery.Price(
                        __typename = "Price",
                        priceInfo = PriceInfo(
                            amount = PriceInfo.Amount(
                                __typename = "Amount",
                                moneyInfo = MoneyInfo(
                                    amount = 100,
                                    amountFormatted = "$100",
                                    currencyCode = "AUS"
                                )
                            ),
                            was = PriceInfo.Was(
                                __typename = "Was",
                                moneyInfo = MoneyInfo(
                                    amount = 200,
                                    amountFormatted = "$200",
                                    currencyCode = "AUS"
                                )
                            )
                        )
                    ),
                    colour = ProductListingQuery.Colour1(
                        __typename = "Colour",
                        colorInfo = ColorInfo(
                            id = "111",
                            name = "blue",
                            swatch = ColorInfo.Swatch(
                                __typename = "Swatch",
                                imageInfo = ImageInfo(
                                    url = "",
                                    width = 50,
                                    height = 50,
                                    alt = "Blue",
                                    mediaContentType = MediaContentType.IMAGE
                                )
                            )
                        )
                    ),
                    size = ProductListingQuery.Size1(
                        __typename = "Size",
                        sizeContainer = SizeContainer(
                            __typename = "SizeContainer",
                            sizeGuide = null,
                            sizeInfo = SizeInfo(
                                id = "789",
                                value = "M",
                                description = "Medium",
                                scale = "Scale"
                            )
                        )
                    ),
                    media = listOf(
                        ProductListingQuery.Medium(
                            __typename = "Image",
                            onImage = ProductListingQuery.OnImage(
                                __typename = "Medium",
                                imageInfo = ImageInfo(
                                    url = "",
                                    width = 250,
                                    height = 150,
                                    alt = "Media",
                                    mediaContentType = MediaContentType.IMAGE
                                )
                            )
                        )
                    ),
                    stock = 100
                )
            ),
            ProductListingQuery.Product(
                id = "654321",
                name = "Product 2",
                shortDescription = "Short description",
                slug = "654321-product",
                styleNumber = "987654321",
                labels = null,
                brand = ProductListingQuery.Brand(
                    __typename = "Brand",
                    brandInfo = BrandInfo(
                        id = "123",
                        name = "Brand",
                        slug = "brand"
                    )
                ),
                priceRange = ProductListingQuery.PriceRange(
                    __typename = "PriceRange",
                    priceRangeInfo = PriceRangeInfo(
                        low = PriceRangeInfo.Low(
                            __typename = "Low",
                            moneyInfo = MoneyInfo(
                                amount = 100,
                                amountFormatted = "$100",
                                currencyCode = "AUS"
                            )
                        ),
                        high = null
                    )
                ),
                colours = null,
                sizes = null,
                defaultVariant = ProductListingQuery.DefaultVariant(
                    price = ProductListingQuery.Price(
                        __typename = "Price",
                        priceInfo = PriceInfo(
                            amount = PriceInfo.Amount(
                                __typename = "Amount",
                                moneyInfo = MoneyInfo(
                                    amount = 100,
                                    amountFormatted = "$100",
                                    currencyCode = "AUS"
                                )
                            ),
                            was = null
                        )
                    ),
                    colour = null,
                    size = null,
                    media = listOf(
                        ProductListingQuery.Medium(
                            __typename = "Image",
                            onImage = ProductListingQuery.OnImage(
                                __typename = "Medium",
                                imageInfo = ImageInfo(
                                    url = "",
                                    width = 250,
                                    height = 150,
                                    alt = null,
                                    mediaContentType = MediaContentType.IMAGE
                                )
                            )
                        )
                    ),
                    stock = 1
                )
            )
        ),
        hierarchy = emptyList(),
        title = "Women"
    )
)

internal val productList = ProductList(
    title = "Women",
    pagination = Pagination(
        limit = 15,
        nextPage = null,
        offset = 0,
        page = 1,
        pageCount = 1,
        previousPage = null,
        total = 2
    ),
    products = listOf(
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
                        width = 50,
                        height = 50,
                        alt = "Blue"
                    )
                )
            ),
            sizes = listOf(
                Size(
                    id = "789",
                    value = "M",
                    description = "Medium",
                    scale = "Scale",
                    sizeGuide = null
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
                color = Color(
                    id = "111",
                    name = "blue",
                    swatch = Media.Image(
                        url = "",
                        width = 50,
                        height = 50,
                        alt = "Blue"
                    )
                ),
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
                        width = 250,
                        height = 150,
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
            sizes = emptyList(),
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
                        width = 250,
                        height = 150,
                        alt = null
                    )
                ),
                stock = 1
            )
        )
    ),
    hierarchy = emptyList()
)
