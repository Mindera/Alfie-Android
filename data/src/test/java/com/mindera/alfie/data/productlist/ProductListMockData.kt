package com.mindera.alfie.data.productlist

import com.mindera.alfie.graphql.ProductListingQuery
import com.mindera.alfie.graphql.fragment.BrandInfo
import com.mindera.alfie.graphql.fragment.ColorInfo
import com.mindera.alfie.graphql.fragment.ImageInfo
import com.mindera.alfie.graphql.fragment.MediaInfo
import com.mindera.alfie.graphql.fragment.MoneyInfo
import com.mindera.alfie.graphql.fragment.PaginationInfo
import com.mindera.alfie.graphql.fragment.PriceInfo
import com.mindera.alfie.graphql.fragment.PriceRangeInfo
import com.mindera.alfie.graphql.fragment.ProductInfo
import com.mindera.alfie.graphql.fragment.SizeContainer
import com.mindera.alfie.graphql.fragment.SizeInfo
import com.mindera.alfie.graphql.fragment.VariantInfo
import com.mindera.alfie.graphql.type.MediaContentType
import com.mindera.alfie.repository.product.model.Color
import com.mindera.alfie.repository.product.model.Price
import com.mindera.alfie.repository.product.model.PriceRange
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListEntryVariant
import com.mindera.alfie.repository.shared.model.Brand
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.Money
import com.mindera.alfie.repository.shared.model.Pagination
import com.mindera.alfie.repository.shared.model.Size

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
                                    )
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
                                id = "111"
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
                            attributes = listOf()
                        )
                    ),
                    variants = listOf(),
                    longDescription = "",
                    attributes = listOf()
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
                                    )
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
                                id = "111"
                            ),
                            size = null,
                            stock = 1,
                            sku = "",
                            attributes = listOf()
                        )
                    ),
                    variants = listOf(),
                    longDescription = "",
                    attributes = listOf()
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
                media = listOf(
                    Media.Image(
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                        alt = "patterson mini skirt"
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
                media = listOf(
                    Media.Image(
                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg",
                        alt = "patterson mini skirt"
                    )
                ),
                stock = 1
            )
        )
    )
)
