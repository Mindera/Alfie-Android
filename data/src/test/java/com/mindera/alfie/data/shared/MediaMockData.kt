package com.mindera.alfie.data.shared

import com.mindera.alfie.graphql.fragment.ImageInfo
import com.mindera.alfie.graphql.fragment.VideoInfo
import com.mindera.alfie.graphql.fragment.VideoSourceInfo
import com.mindera.alfie.graphql.type.MediaContentType
import com.mindera.alfie.graphql.type.VideoFormat
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.VideoSource
import com.mindera.alfie.repository.shared.model.VideoFormat as InternalVideoFormat

internal val imageInfoData = ImageInfo(
    alt = "patterson mini skirt",
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
    url = "https://www.alfie.com/productimages/thumb/2/2666503_22841458_13891527.jpg"
)

internal val video = Media.Video(
    alt = "patterson mini skirt",
    previewImage = Media.Image(
        alt = "patterson mini skirt",
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
