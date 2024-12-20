package au.com.alfie.ecomm.data.product

import au.com.alfie.ecomm.graphql.ProductQuery
import au.com.alfie.ecomm.graphql.fragment.BrandInfo
import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.graphql.fragment.HierarchyItemInfo
import au.com.alfie.ecomm.graphql.fragment.HierarchyItemsTree
import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.MediaInfo
import au.com.alfie.ecomm.graphql.fragment.MoneyInfo
import au.com.alfie.ecomm.graphql.fragment.PriceInfo
import au.com.alfie.ecomm.graphql.fragment.SizeContainer
import au.com.alfie.ecomm.graphql.fragment.SizeInfo
import au.com.alfie.ecomm.graphql.fragment.VariantInfo
import au.com.alfie.ecomm.graphql.type.MediaContentType
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.product.model.Variant
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.HierarchyItem
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.Money
import au.com.alfie.ecomm.repository.shared.model.Size

internal val productData = ProductQuery.Data(
    product = ProductQuery.Product(
        id = "2666503",
        attributes = null,
        brand = ProductQuery.Brand(
            __typename = "",
            brandInfo = BrandInfo(
                id = "1",
                name = "Camilla and Marc",
                slug = "camilla-and-marc"
            )
        ),
        defaultVariant = ProductQuery.DefaultVariant(
            __typename = "",
            variantInfo = VariantInfo(
                sku = "UNKNOWN",
                size = VariantInfo.Size(
                    __typename = "",
                    sizeContainer = SizeContainer(
                        __typename = "",
                        sizeGuide = null,
                        sizeInfo = SizeInfo(
                            id = "25927",
                            value = "10 AU",
                            scale = null,
                            description = "10 AU"
                        )
                    )
                ),
                colour = VariantInfo.Colour(
                    __typename = "",
                    colorInfo = ColorInfo(
                        id = "3406543",
                        swatch = null,
                        name = "steel"
                    )
                ),
                media = listOf(
                    VariantInfo.Medium(
                        __typename = "",
                        mediaInfo = MediaInfo(
                            __typename = "",
                            onImage = MediaInfo.OnImage(
                                __typename = "",
                                imageInfo = ImageInfo(
                                    alt = "patterson mini skirt",
                                    width = 487,
                                    height = 634,
                                    mediaContentType = MediaContentType.IMAGE,
                                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                )
                            ),
                            onVideo = null
                        )
                    ),
                    VariantInfo.Medium(
                        __typename = "",
                        mediaInfo = MediaInfo(
                            __typename = "",
                            onImage = MediaInfo.OnImage(
                                __typename = "",
                                imageInfo = ImageInfo(
                                    alt = "patterson mini skirt",
                                    width = 487,
                                    height = 634,
                                    mediaContentType = MediaContentType.IMAGE,
                                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                                )
                            ),
                            onVideo = null
                        )
                    )
                ),
                attributes = null,
                stock = 100,
                price = VariantInfo.Price(
                    __typename = "",
                    priceInfo = PriceInfo(
                        amount = PriceInfo.Amount(
                            __typename = "",
                            moneyInfo = MoneyInfo(
                                currencyCode = "AUD",
                                amount = 40000,
                                amountFormatted = "$400.00"
                            )
                        ),
                        was = null
                    )
                )
            )
        ),
        hierarchy = listOf(
            ProductQuery.Hierarchy(
                __typename = "",
                hierarchyItemsTree = HierarchyItemsTree(
                    __typename = "",
                    hierarchyItemInfo = HierarchyItemInfo(
                        categoryId = "1",
                        name = "women",
                        slug = "/women"
                    )
                )
            ),
            ProductQuery.Hierarchy(
                __typename = "",
                hierarchyItemsTree = HierarchyItemsTree(
                    __typename = "",
                    hierarchyItemInfo = HierarchyItemInfo(
                        categoryId = "1",
                        name = "clothing",
                        slug = "/women/clothing"
                    )
                )
            ),
            ProductQuery.Hierarchy(
                __typename = "",
                hierarchyItemsTree = HierarchyItemsTree(
                    __typename = "",
                    hierarchyItemInfo = HierarchyItemInfo(
                        categoryId = "1",
                        name = "patterson mini skirt",
                        slug = "camilla-and-marc-patterson-mini-skirt-26531650"
                    )
                )
            )
        ),
        labels = null,
        longDescription = "Long description",
        name = "patterson mini skirt",
        priceRange = null,
        shortDescription = "Short description",
        sizes = listOf(
            ProductQuery.Size(
                __typename = "",
                sizeContainer = SizeContainer(
                    __typename = "",
                    sizeGuide = null,
                    sizeInfo = SizeInfo(
                        id = "25927",
                        description = "10 AU",
                        scale = null,
                        value = "10 AU"
                    )
                )
            ),
            ProductQuery.Size(
                __typename = "",
                sizeContainer = SizeContainer(
                    __typename = "",
                    sizeGuide = null,
                    sizeInfo = SizeInfo(
                        id = "25937",
                        description = "12 AU",
                        scale = null,
                        value = "12 AU"
                    )
                )
            ),
            ProductQuery.Size(
                __typename = "",
                sizeContainer = SizeContainer(
                    __typename = "",
                    sizeGuide = null,
                    sizeInfo = SizeInfo(
                        id = "25974",
                        description = "6 AU",
                        scale = null,
                        value = "6 AU"
                    )
                )
            ),
            ProductQuery.Size(
                __typename = "",
                sizeContainer = SizeContainer(
                    __typename = "",
                    sizeGuide = null,
                    sizeInfo = SizeInfo(
                        id = "25988",
                        description = "8 AU",
                        scale = null,
                        value = "8 AU"
                    )
                )
            )
        ),
        slug = "camilla-and-marc-patterson-mini-skirt-26531650",
        styleNumber = "26531650",
        variants = listOf(
            ProductQuery.Variant(
                __typename = "",
                variantInfo = VariantInfo(
                    sku = "UNKNOWN",
                    size = VariantInfo.Size(
                        __typename = "",
                        sizeContainer = SizeContainer(
                            __typename = "",
                            sizeGuide = null,
                            sizeInfo = SizeInfo(
                                id = "25927",
                                value = "10 AU",
                                scale = null,
                                description = "10 AU"
                            )
                        )
                    ),
                    colour = VariantInfo.Colour(
                        __typename = "",
                        colorInfo = ColorInfo(
                            id = "3406543",
                            swatch = null,
                            name = "steel"
                        )
                    ),
                    media = listOf(
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        ),
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        )
                    ),
                    attributes = null,
                    stock = 100,
                    price = VariantInfo.Price(
                        __typename = "",
                        priceInfo = PriceInfo(
                            amount = PriceInfo.Amount(
                                __typename = "",
                                moneyInfo = MoneyInfo(
                                    currencyCode = "AUD",
                                    amount = 40000,
                                    amountFormatted = "$400.00"
                                )
                            ),
                            was = null
                        )
                    )
                )
            ),
            ProductQuery.Variant(
                __typename = "",
                variantInfo = VariantInfo(
                    sku = "UNKNOWN",
                    size = VariantInfo.Size(
                        __typename = "",
                        sizeContainer = SizeContainer(
                            __typename = "",
                            sizeGuide = null,
                            sizeInfo = SizeInfo(
                                id = "25937",
                                value = "12 AU",
                                scale = null,
                                description = "12 AU"
                            )
                        )
                    ),
                    colour = VariantInfo.Colour(
                        __typename = "",
                        colorInfo = ColorInfo(
                            id = "3406543",
                            swatch = null,
                            name = "steel"
                        )
                    ),
                    media = listOf(
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        ),
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        )
                    ),
                    attributes = null,
                    stock = 100,
                    price = VariantInfo.Price(
                        __typename = "",
                        priceInfo = PriceInfo(
                            amount = PriceInfo.Amount(
                                __typename = "",
                                moneyInfo = MoneyInfo(
                                    currencyCode = "AUD",
                                    amount = 40000,
                                    amountFormatted = "$400.00"
                                )
                            ),
                            was = null
                        )
                    )
                )
            ),
            ProductQuery.Variant(
                __typename = "",
                variantInfo = VariantInfo(
                    sku = "UNKNOWN",
                    size = VariantInfo.Size(
                        __typename = "",
                        sizeContainer = SizeContainer(
                            __typename = "",
                            sizeGuide = null,
                            sizeInfo = SizeInfo(
                                id = "25974",
                                value = "6 AU",
                                scale = null,
                                description = "6 AU"
                            )
                        )
                    ),
                    colour = VariantInfo.Colour(
                        __typename = "",
                        colorInfo = ColorInfo(
                            id = "3406543",
                            swatch = null,
                            name = "steel"
                        )
                    ),
                    media = listOf(
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        ),
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        )
                    ),
                    attributes = null,
                    stock = 100,
                    price = VariantInfo.Price(
                        __typename = "",
                        priceInfo = PriceInfo(
                            amount = PriceInfo.Amount(
                                __typename = "",
                                moneyInfo = MoneyInfo(
                                    currencyCode = "AUD",
                                    amount = 40000,
                                    amountFormatted = "$400.00"
                                )
                            ),
                            was = null
                        )
                    )
                )
            ),
            ProductQuery.Variant(
                __typename = "",
                variantInfo = VariantInfo(
                    sku = "UNKNOWN",
                    size = VariantInfo.Size(
                        __typename = "",
                        sizeContainer = SizeContainer(
                            __typename = "",
                            sizeGuide = null,
                            sizeInfo = SizeInfo(
                                id = "25988",
                                value = "8 AU",
                                scale = null,
                                description = "8 AU"
                            )
                        )
                    ),
                    colour = VariantInfo.Colour(
                        __typename = "",
                        colorInfo = ColorInfo(
                            id = "3406557",
                            swatch = null,
                            name = "steel"
                        )
                    ),
                    media = listOf(
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        ),
                        VariantInfo.Medium(
                            __typename = "",
                            mediaInfo = MediaInfo(
                                __typename = "",
                                onImage = MediaInfo.OnImage(
                                    __typename = "",
                                    imageInfo = ImageInfo(
                                        alt = "patterson mini skirt",
                                        width = 487,
                                        height = 634,
                                        mediaContentType = MediaContentType.IMAGE,
                                        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                                    )
                                ),
                                onVideo = null
                            )
                        )
                    ),
                    attributes = null,
                    stock = 100,
                    price = VariantInfo.Price(
                        __typename = "",
                        priceInfo = PriceInfo(
                            amount = PriceInfo.Amount(
                                __typename = "",
                                moneyInfo = MoneyInfo(
                                    currencyCode = "AUD",
                                    amount = 40000,
                                    amountFormatted = "$400.00"
                                )
                            ),
                            was = null
                        )
                    )
                )
            )
        )
    )
)

internal val product = Product(
    id = "2666503",
    attributes = emptyList(),
    brand = Brand(
        id = "1",
        name = "Camilla and Marc",
        slug = "camilla-and-marc"
    ),
    defaultVariant = Variant(
        attributes = emptyList(),
        sku = "UNKNOWN",
        size = Size(
            id = "25927",
            value = "10 AU",
            scale = null,
            description = "10 AU",
            sizeGuide = null
        ),
        color = Color(
            id = "3406543",
            swatch = null,
            name = "steel"
        ),
        media = listOf(
            Media.Image(
                alt = "patterson mini skirt",
                width = 487,
                height = 634,
                url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
            ),
            Media.Image(
                alt = "patterson mini skirt",
                width = 487,
                height = 634,
                url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
            )
        ),
        stock = 100,
        price = Price(
            amount = Money(
                currencyCode = "AUD",
                amount = 40000,
                amountFormatted = "$400.00"
            ),
            was = null
        )
    ),
    hierarchy = listOf(
        HierarchyItem(
            categoryId = "1",
            name = "women",
            slug = "/women",
            parent = null
        ),
        HierarchyItem(
            categoryId = "1",
            name = "clothing",
            slug = "/women/clothing",
            parent = null
        ),
        HierarchyItem(
            categoryId = "1",
            name = "patterson mini skirt",
            slug = "camilla-and-marc-patterson-mini-skirt-26531650",
            parent = null
        )
    ),
    labels = emptyList(),
    longDescription = "Long description",
    name = "patterson mini skirt",
    priceRange = null,
    shortDescription = "Short description",
    sizes = listOf(
        Size(
            id = "25927",
            description = "10 AU",
            scale = null,
            value = "10 AU",
            sizeGuide = null
        ),
        Size(
            id = "25937",
            description = "12 AU",
            scale = null,
            value = "12 AU",
            sizeGuide = null
        ),
        Size(
            id = "25974",
            description = "6 AU",
            scale = null,
            value = "6 AU",
            sizeGuide = null
        ),
        Size(
            id = "25988",
            description = "8 AU",
            scale = null,
            value = "8 AU",
            sizeGuide = null
        )
    ),
    slug = "camilla-and-marc-patterson-mini-skirt-26531650",
    styleNumber = "26531650",
    variants = listOf(
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25927",
                value = "10 AU",
                scale = null,
                description = "10 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406543",
                swatch = null,
                name = "steel"
            ),
            media = listOf(
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                ),
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                )
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        ),
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25937",
                value = "12 AU",
                scale = null,
                description = "12 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406543",
                swatch = null,
                name = "steel"
            ),
            media = listOf(
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                ),
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                )
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        ),
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25974",
                value = "6 AU",
                scale = null,
                description = "6 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406543",
                swatch = null,
                name = "steel"
            ),
            media = listOf(
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                ),
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                )
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        ),
        Variant(
            attributes = emptyList(),
            sku = "UNKNOWN",
            size = Size(
                id = "25988",
                value = "8 AU",
                scale = null,
                description = "8 AU",
                sizeGuide = null
            ),
            color = Color(
                id = "3406557",
                swatch = null,
                name = "steel"
            ),
            media = listOf(
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/1/2666503_22841458_13891526.jpg"
                ),
                Media.Image(
                    alt = "patterson mini skirt",
                    width = 487,
                    height = 634,
                    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
                )
            ),
            stock = 100,
            price = Price(
                amount = Money(
                    currencyCode = "AUD",
                    amount = 40000,
                    amountFormatted = "$400.00"
                ),
                was = null
            )
        )
    )
)
