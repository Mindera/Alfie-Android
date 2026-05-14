package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.graphql.fragment.ImageInfo
import com.mindera.alfie.graphql.fragment.MediaInfo
import com.mindera.alfie.graphql.fragment.VideoInfo
import com.mindera.alfie.graphql.fragment.VideoSourceInfo
import com.mindera.alfie.repository.shared.model.Media
import com.mindera.alfie.repository.shared.model.VideoFormat
import com.mindera.alfie.repository.shared.model.VideoSource

internal fun ImageInfo.toDomain() = Media.Image(
    alt = alt,
    url = url.toString()
)

internal fun MediaInfo.toDomain() =
    this.onImage?.imageInfo?.toDomain() ?: this.onVideo?.videoInfo?.toDomain()

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
