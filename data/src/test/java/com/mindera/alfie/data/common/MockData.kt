package com.mindera.alfie.data.common

import com.mindera.alfie.graphql.fragment.ColorInfo
import com.mindera.alfie.graphql.fragment.ImageInfo
import com.mindera.alfie.graphql.fragment.MediaInfo
import com.mindera.alfie.graphql.fragment.ProductInfo
import com.mindera.alfie.graphql.type.MediaContentType
import com.mindera.alfie.repository.product.model.Color
import com.mindera.alfie.repository.shared.model.Media

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
            )
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