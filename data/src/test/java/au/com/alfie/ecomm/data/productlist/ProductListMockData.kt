package au.com.alfie.ecomm.data.productlist

import au.com.alfie.ecomm.graphql.ProductListingQuery
import au.com.alfie.ecomm.graphql.fragment.BrandInfo
import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.MediaInfo
import au.com.alfie.ecomm.graphql.fragment.MoneyInfo
import au.com.alfie.ecomm.graphql.fragment.PaginationInfo
import au.com.alfie.ecomm.graphql.fragment.PriceInfo
import au.com.alfie.ecomm.graphql.fragment.PriceRangeInfo
import au.com.alfie.ecomm.graphql.fragment.ProductInfo
import au.com.alfie.ecomm.graphql.fragment.SizeContainer
import au.com.alfie.ecomm.graphql.fragment.SizeInfo
import au.com.alfie.ecomm.graphql.fragment.VariantInfo
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
            __typename = "",
            paginationInfo = PaginationInfo(
                limit = 15,
                nextPage = null,
                offset = 0,
                page = 1,
                pages = 1,
                previousPage = null,
                total = 2
            )
        ),
        products = listOf(
            ProductListingQuery.Product(
                __typename = "",
                productInfo = ProductInfo(
                    id = "123456",
                    name = "Product name",
                    shortDescription = "Short description",
                    slug = "123456-product",
                    styleNumber = "123456789",
                    labels = listOf("Label"),
                    brand = ProductInfo.Brand(
                        __typename = "Brand",
                        brandInfo = BrandInfo(
                            id = "123",
                            name = "Brand",
                            slug = "brand"
                        )

                    ),
                    priceRange = ProductInfo.PriceRange(
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
                        ProductInfo.Colour(
                            __typename = "Colour",
                            colorInfo = ColorInfo(
                                id = "111",
                                name = "blue",
                                swatch = ColorInfo.Swatch(
                                    __typename = "Swatch",
                                    imageInfo = ImageInfo(
                                        url = "",
                                        alt = "Blue",
                                        mediaContentType = MediaContentType.IMAGE
                                    )
                                ),
                                media = listOf(
                                    ColorInfo.Medium(
                                        __typename = "",
                                        mediaInfo = MediaInfo(
                                            __typename = "",
                                            onImage = MediaInfo.OnImage(
                                                __typename = "",
                                                imageInfo = ImageInfo(
                                                    alt = "patterson mini skirt",
                                                    mediaContentType = MediaContentType.IMAGE,
                                                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                                )
                                            ),
                                            onVideo = null
                                        )
                                    ),
                                )
                            )
                        )
                    ),
                    defaultVariant = ProductInfo.DefaultVariant(
                        __typename = "Price",
                        variantInfo = VariantInfo(
                            price = VariantInfo.Price(
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
                            colour = VariantInfo.Colour(
                                id = "111",
                            ),
                            size = VariantInfo.Size(
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
                            stock = 100,
                            sku = "",
                            attributes = listOf(),
                        ),
                    ),
                    variants = listOf(),
                    longDescription = "",
                    attributes = listOf(),
                )
            ),
            ProductListingQuery.Product(
                __typename = "",
                productInfo = ProductInfo(
                    id = "654321",
                    name = "Product 2",
                    shortDescription = "Short description",
                    slug = "654321-product",
                    styleNumber = "987654321",
                    labels = null,
                    brand = ProductInfo.Brand(
                        __typename = "Brand",
                        brandInfo = BrandInfo(
                            id = "123",
                            name = "Brand",
                            slug = "brand"
                        )
                    ),
                    priceRange = ProductInfo.PriceRange(
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
                    colours = listOf(
                        ProductInfo.Colour(
                            __typename = "Colour",
                            colorInfo = ColorInfo(
                                id = "111",
                                name = "blue",
                                swatch = ColorInfo.Swatch(
                                    __typename = "Swatch",
                                    imageInfo = ImageInfo(
                                        url = "",
                                        alt = "Blue",
                                        mediaContentType = MediaContentType.IMAGE
                                    )
                                ),
                                media = listOf(
                                    ColorInfo.Medium(
                                        __typename = "",
                                        mediaInfo = MediaInfo(
                                            __typename = "",
                                            onImage = MediaInfo.OnImage(
                                                __typename = "",
                                                imageInfo = ImageInfo(
                                                    alt = "patterson mini skirt",
                                                    mediaContentType = MediaContentType.IMAGE,
                                                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                                )
                                            ),
                                            onVideo = null
                                        )
                                    ),
                                )
                            )
                        )
                    ),
                    defaultVariant = ProductInfo.DefaultVariant(
                        __typename = "Price",
                        variantInfo = VariantInfo(
                            price = VariantInfo.Price(
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
                            colour = VariantInfo.Colour(
                                id = "111",
                            ),
                            size = null,
                            stock = 1,
                            sku = "",
                            attributes = listOf(),
                        ),
                    ),
                    variants = listOf(),
                    longDescription = "",
                    attributes = listOf(),
                )
            )
        ),
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
                        alt = "Blue"
                    ),
                    media = listOf(
                        Media.Image(
                            url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                            alt = "patterson mini skirt"
                        )
                    )
                )
            ),
            longDescription = "",
            attributes = listOf(),
            variants = listOf(),
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
                media = Media.Image(
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                    alt = "patterson mini skirt"
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
                            url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                            alt = "patterson mini skirt"
                        )
                    )
                )
            ),
            longDescription = "",
            attributes = listOf(),
            variants = listOf(),
            defaultVariant = ProductListEntryVariant(
                price = Price(
                    amount = Money(
                        amount = 100,
                        amountFormatted = "$100",
                        currencyCode = "AUS"
                    ),
                    was = null
                ),
                color = "111",
                size = null,
                media = Media.Image(
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                    alt = "patterson mini skirt"
                ),
                stock = 1
            )
        )
    ),
)
