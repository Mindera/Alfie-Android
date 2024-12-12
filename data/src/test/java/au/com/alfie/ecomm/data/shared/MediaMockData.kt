package au.com.alfie.ecomm.data.shared

import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.VideoInfo
import au.com.alfie.ecomm.graphql.fragment.VideoSourceInfo
import au.com.alfie.ecomm.graphql.type.MediaContentType
import au.com.alfie.ecomm.graphql.type.VideoFormat
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.VideoSource
import au.com.alfie.ecomm.repository.shared.model.VideoFormat as InternalVideoFormat

internal val imageInfoData = ImageInfo(
    alt = "patterson mini skirt",
    width = 487,
    height = 634,
    mediaContentType = MediaContentType.IMAGE,
    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
)

internal val videoInfoData = VideoInfo(
    alt = "patterson mini skirt",
    mediaContentType = MediaContentType.VIDEO,
    previewImage = VideoInfo.PreviewImage(
        __typename = "",
        imageInfo = ImageInfo(
            alt = "patterson mini skirt",
            width = 487,
            height = 634,
            mediaContentType = MediaContentType.IMAGE,
            url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
        )
    ),
    sources = listOf(
        VideoInfo.Source(
            __typename = "",
            videoSourceInfo = VideoSourceInfo(
                format = VideoFormat.MP4,
                mimeType = "mimetype",
                url = "url",
                width = 487,
                height = 634
            )
        )
    )
)

internal val image = Media.Image(
    alt = "patterson mini skirt",
    width = 487,
    height = 634,
    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
)

internal val video = Media.Video(
    alt = "patterson mini skirt",
    previewImage = Media.Image(
        alt = "patterson mini skirt",
        width = 487,
        height = 634,
        url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
    ),
    sources = listOf(
        VideoSource(
            format = InternalVideoFormat.MP4,
            mimeType = "mimetype",
            url = "url",
            width = 487,
            height = 634
        )
    )
)
