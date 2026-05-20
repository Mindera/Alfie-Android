package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.data.shared.image
import com.mindera.alfie.data.shared.imageInfoData
import com.mindera.alfie.data.shared.video
import com.mindera.alfie.data.shared.videoInfoData
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class MediaMapperTest {
    @Test
    fun testImageInfoMapper() = runTest {
        val expected = image
        val result = imageInfoData.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun testVideoInfoMapper() = runTest {
        val expected = video
        val result = videoInfoData.toDomain()

        assertEquals(expected, result)
    }
}
