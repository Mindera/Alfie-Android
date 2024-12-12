package au.com.alfie.ecomm.debug.nonoperational.interceptor

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DebugInterceptorsNonOpTest {

    @InjectMockKs
    lateinit var subject: DebugInterceptorsNonOp

    @Test
    fun `invoke - then should return empty`() {
        assert(subject().isEmpty())
    }
}
