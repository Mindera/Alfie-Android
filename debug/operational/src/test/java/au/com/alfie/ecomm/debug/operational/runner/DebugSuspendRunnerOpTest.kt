package au.com.alfie.ecomm.debug.operational.runner

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DebugSuspendRunnerOpTest {

    @InjectMockKs
    lateinit var subject: DebugSuspendRunnerOp

    @Test
    fun `invoke - then should run the block`() = runTest {
        val block = mockk<() -> Unit>(relaxed = true)

        subject(block)

        verify(exactly = 1) {
            block.invoke()
        }
    }

    @Test
    fun `invoke - given onDebug and onRelease then should run only onDebug`() = runTest {
        val onDebug = mockk<() -> Unit>(relaxed = true)
        val onRelease = mockk<() -> Unit>()

        subject(onDebug = onDebug, onRelease = onRelease)

        verify(exactly = 1) {
            onDebug.invoke()
        }
        verify(exactly = 0) {
            onRelease.invoke()
        }
    }
}
