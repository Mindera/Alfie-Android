package au.com.alfie.ecomm.core.commons.util.sync

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class SynchronizerTest {

    @RelaxedMockK
    private lateinit var syncable: Syncable

    @Test
    fun `sync - the synchronizer is passed to syncWith`() = runTest {
        coEvery { syncable.syncWith(any()) } returns true

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.run {
            syncable.sync()
        }

        assertTrue(result)
        coVerify { syncable.syncWith(synchronizer) }
    }

    @Test
    fun `syncWithResult - when does not throw exception then returns success`() = runTest {
        val block: suspend () -> String = mockk()
        coEvery { block.invoke() } returns "value"

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.syncWithResult(block)

        assertTrue(result.isSuccess)
        assertEquals("value", result.getOrNull())
    }

    @Test
    fun `syncWithResult - when throw exception then returns error`() = runTest {
        val error = mockk<Exception>()
        val block: suspend () -> String = mockk()
        coEvery { block.invoke() } throws error

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.syncWithResult(block)

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `updateCache - when has data then calls update`() = runTest {
        val remote: suspend () -> List<String> = mockk()
        coEvery { remote.invoke() } returns listOf("value")
        val update: suspend (List<String>) -> Unit = mockk(relaxed = true)

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.updateCache(
            remote = remote,
            update = update
        )

        assertTrue(result.isSuccess)
        coVerify { update.invoke(any()) }
    }

    @Test
    fun `updateCache - when does not have data then does not calls update`() = runTest {
        val remote: suspend () -> List<String> = mockk()
        coEvery { remote.invoke() } returns emptyList()
        val update: suspend (List<String>) -> Unit = mockk(relaxed = true)

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.updateCache(
            remote = remote,
            update = update
        )

        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { update.invoke(any()) }
    }

    @Test
    fun `updateCache - when remote fails then return error`() = runTest {
        val error = mockk<Exception>()
        val remote: suspend () -> List<String> = mockk()
        coEvery { remote.invoke() } throws error
        val update: suspend (List<String>) -> Unit = mockk(relaxed = true)

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.updateCache(
            remote = remote,
            update = update
        )

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `updateCacheElement - when there is no failure then returns success`() = runTest {
        val remote: suspend () -> String = mockk()
        coEvery { remote.invoke() } returns "value"
        val update: suspend (String) -> Unit = mockk(relaxed = true)

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.updateCacheElement(
            remote = remote,
            update = update
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateCacheElement - when something fails then returns error`() = runTest {
        val error = mockk<Exception>()
        val remote: suspend () -> String = mockk()
        coEvery { remote.invoke() } throws error
        val update: suspend (String) -> Unit = mockk(relaxed = true)

        val synchronizer: Synchronizer = TestSynchronizer()

        val result = synchronizer.updateCacheElement(
            remote = remote,
            update = update
        )

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    private class TestSynchronizer : Synchronizer
}
