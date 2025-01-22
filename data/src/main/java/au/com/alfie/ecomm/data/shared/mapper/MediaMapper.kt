package au.com.alfie.ecomm.data.shared.mapper

import au.com.alfie.ecomm.graphql.fragment.ImageInfo
import au.com.alfie.ecomm.graphql.fragment.MediaInfo
import au.com.alfie.ecomm.graphql.fragment.VideoInfo
import au.com.alfie.ecomm.graphql.fragment.VideoSourceInfo
import au.com.alfie.ecomm.repository.shared.model.Media
import au.com.alfie.ecomm.repository.shared.model.VideoFormat
import au.com.alfie.ecomm.repository.shared.model.VideoSource

internal fun ImageInfo.toDomain() = Media.Image(
    alt = alt,
    url = url.toString(),
)

internal fun MediaInfo.toDomain(): Media? {
    return this.onImage?.imageInfo?.toDomain() ?: this.onVideo?.videoInfo?.toDomain()
}

internal fun VideoInfo.toDomain() = Media.Video(
    alt = alt,
    sources = sources.map { it.videoSourceInfo.toDomain() },
    previewImage = previewImage?.imageInfo?.toDomain()
)

private fun VideoSourceInfo.toDomain() = VideoSource(
    format = VideoFormat.fromValue(format.name),
    mimeType = mimeType,
    url = url.toString(),
    width = width,
    height = height
)
