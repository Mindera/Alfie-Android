package au.com.alfie.ecomm.debug.nonoperational.runner

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DebugRunnerNonOpTest {

    @InjectMockKs
    lateinit var subject: DebugRunnerNonOp

    @Test
    fun `invoke - then should not run`() {
        val block = mockk<() -> Unit>()

        subject(block)

        verify(exactly = 0) {
            block.invoke()
        }
    }

    @Test
    fun `invoke - given onDebug and onRelease then should run only onRelease`() {
        val onDebug = mockk<() -> Unit>()
        val onRelease = mockk<() -> Unit>(relaxed = true)

        subject(onDebug = onDebug, onRelease = onRelease)

        verify(exactly = 0) {
            onDebug.invoke()
        }
        verify(exactly = 1) {
            onRelease.invoke()
        }
    }
}
