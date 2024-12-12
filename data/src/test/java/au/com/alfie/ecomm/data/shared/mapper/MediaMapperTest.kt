package au.com.alfie.ecomm.data.shared.mapper

import au.com.alfie.ecomm.data.shared.image
import au.com.alfie.ecomm.data.shared.imageInfoData
import au.com.alfie.ecomm.data.shared.video
import au.com.alfie.ecomm.data.shared.videoInfoData
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
