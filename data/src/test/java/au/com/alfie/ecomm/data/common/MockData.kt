package au.com.alfie.ecomm.data.common

import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.MediaInfo
import au.com.alfie.ecomm.graphql.fragment.ProductInfo
import au.com.alfie.ecomm.graphql.type.MediaContentType
import au.com.alfie.ecomm.repository.product.model.Color
import au.com.alfie.ecomm.repository.shared.model.Media

val productInfoColor = ProductInfo.Colour(
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

val color =
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