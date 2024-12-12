package au.com.alfie.ecomm.data.datastore.debug

import androidx.datastore.core.DataStore
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.core.test.TestDispatcherProvider
import au.com.alfie.ecomm.data.datastore.DebugPreferencesProto
import au.com.alfie.ecomm.data.datastore.DebugPreferencesProto.EnvironmentProto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class DebugPreferencesDataSourceImplTest {

    private val dataStore: DataStore<DebugPreferencesProto> = mockk(relaxed = true)

    private val dispatcherProvider = TestDispatcherProvider()

    private val subject = DebugPreferencesDataSourceImpl(
        dataStore = dataStore,
        dispatcherProvider = dispatcherProvider
    )

    @Test
    fun `updateEnvironment - then call update data from data store`() = runTest {
        subject.updateEnvironment { EnvironmentProto.DEV }

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `updateCustomUrl - then call update data from data store`() = runTest {
        subject.updateCustomUrl("url")

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `getEnvironment - then get data from data store`() = runTest {
        val debugPreferencesProto = mockk<DebugPreferencesProto>() {
            every { currentEnvironment } returns EnvironmentProto.CUSTOM
            every { customUrl } returns "customUrl"
        }
        coEvery { dataStore.data } returns flowOf(debugPreferencesProto)

        val map = mockk<(EnvironmentProto, String?) -> Unit>(relaxed = true)

        subject.getEnvironment(map)

        verify { map(EnvironmentProto.CUSTOM, "customUrl") }
    }

    @Test
    fun `getCustomUrl - then get data from data store`() = runTest {
        val debugPreferencesProto = mockk<DebugPreferencesProto>() {
            every { currentEnvironment } returns EnvironmentProto.CUSTOM
            every { customUrl } returns "customUrl"
        }
        coEvery { dataStore.data } returns flowOf(debugPreferencesProto)

        val result = subject.getCustomUrl()

        assertEquals("customUrl", result)
    }
}
