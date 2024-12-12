package au.com.alfie.ecomm.debug.operational.interceptor

import com.chuckerteam.chucker.api.ChuckerInterceptor
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import okhttp3.Interceptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class DebugInterceptorsOpTest {

    @RelaxedMockK
    lateinit var chuckerCreator: ChuckerCreator

    @InjectMockKs
    lateinit var subject: DebugInterceptorsOp

    @Test
    fun `invoke - then should return interceptors`() {
        val chucker = mockk<ChuckerInterceptor>()
        every { chuckerCreator.create() } returns chucker

        val result: List<Interceptor> = subject()

        assertEquals(1, result.size)
        assertEquals(chucker, result[0])
    }
}
