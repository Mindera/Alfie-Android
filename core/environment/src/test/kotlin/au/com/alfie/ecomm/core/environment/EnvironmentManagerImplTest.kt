package au.com.alfie.ecomm.core.environment

import au.com.alfie.ecomm.core.environment.model.BuildConfiguration
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.environment.model.Environments
import au.com.alfie.ecomm.core.test.debug.TestDebugSuspendRunner
import au.com.alfie.ecomm.data.datastore.DebugPreferencesProto.EnvironmentProto
import au.com.alfie.ecomm.data.datastore.debug.DebugPreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val WEB_URL = "www.alfie.com"
private const val DEV_URL = "devUrl"
private const val PRE_PROD_URL = "preProdUrl"
private const val PROD_URL = "prodUrl"
private const val CUSTOM_URL = "customUrl"

@ExtendWith(MockKExtension::class)
internal class EnvironmentManagerImplTest {

    private val debugPreferencesDataSource: DebugPreferencesDataSource = mockk()

    private val debugRunner = TestDebugSuspendRunner(false)

    private val subject = EnvironmentManagerImpl(
        debugPreferencesDataSource = debugPreferencesDataSource,
        buildConfiguration = createBuildConfiguration(),
        debugRunner = debugRunner
    )

    @Test
    fun `current - given release build then should return prod url`() = runTest {
        debugRunner.isRelease = true

        val result = subject.current()

        assertTrue { result is Environment.Prod }
        assertEquals(PROD_URL, result.graphQLUrl)
    }

    @Test
    fun `current - given debug when the proto is dev then should return dev url`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.DEV, null)
        }

        val result = subject.current()

        assertTrue { result is Environment.Dev }
        assertEquals(DEV_URL, result.graphQLUrl)
    }

    @Test
    fun `current - given debug when the proto is unrecognized then should return dev url`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.UNRECOGNIZED, null)
        }

        val result = subject.current()

        assertTrue { result is Environment.Dev }
        assertEquals(DEV_URL, result.graphQLUrl)
    }

    @Test
    fun `current - given debug when the proto is pre prod then should return pre prod url`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.PRE_PROD, null)
        }

        val result = subject.current()

        assertTrue { result is Environment.PreProd }
        assertEquals(PRE_PROD_URL, result.graphQLUrl)
    }

    @Test
    fun `current - given debug when the proto is prod then should return prod url`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.PROD, null)
        }

        val result = subject.current()

        assertTrue { result is Environment.Prod }
        assertEquals(PROD_URL, result.graphQLUrl)
    }

    @Test
    fun `current - given debug when the proto is custom then should return custom url`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.CUSTOM, CUSTOM_URL)
        }

        val result = subject.current()

        assertTrue { result is Environment.Custom }
        assertEquals(CUSTOM_URL, result.graphQLUrl)
    }

    @Test
    fun `environments - then should return the environments from the build config`() {
        val expected = Environments(
            dev = Environment.Dev(graphQLUrl = DEV_URL, webUrl = WEB_URL),
            preProd = Environment.PreProd(graphQLUrl = PRE_PROD_URL, webUrl = WEB_URL),
            prod = Environment.Prod(graphQLUrl = PROD_URL, webUrl = WEB_URL)
        )

        val result = subject.environments()

        assertEquals(expected, result)
    }

    @Test
    fun `custom - given the current env is custom then should return the custom environment stored`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.CUSTOM, CUSTOM_URL)
        }

        val result = subject.custom()

        assertEquals(CUSTOM_URL, result.graphQLUrl)
    }

    @Test
    fun `custom - given the current env is not custom then should return the custom url stored`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.DEV, "")
        }
        coEvery { debugPreferencesDataSource.getCustomUrl() } returns CUSTOM_URL

        val result = subject.custom()

        assertEquals(CUSTOM_URL, result.graphQLUrl)
    }

    @Test
    fun `custom - given the current env is not and the custom url is empty then should return the default url`() = runTest {
        debugRunner.isRelease = false
        val slot = slot<(EnvironmentProto, String?) -> Environment>()
        coEvery { debugPreferencesDataSource.getEnvironment(capture(slot)) } coAnswers {
            slot.captured.invoke(EnvironmentProto.DEV, "")
        }
        coEvery { debugPreferencesDataSource.getCustomUrl() } returns ""

        val result = subject.custom()

        assertEquals("https://api-preview-000.localhost:4000/graphql", result.graphQLUrl)
    }

    @Test
    fun `update - given env dev then should map to proto dev`() = runTest {
        val slot = slot<() -> EnvironmentProto>()
        coEvery { debugPreferencesDataSource.updateEnvironment(capture(slot)) } returns Unit

        subject.update(Environment.Dev(graphQLUrl = DEV_URL, webUrl = WEB_URL))

        val result = slot.captured.invoke()

        assertEquals(EnvironmentProto.DEV, result)
    }

    @Test
    fun `update - given env pre prod then should map to proto pre prod`() = runTest {
        val slot = slot<() -> EnvironmentProto>()
        coEvery { debugPreferencesDataSource.updateEnvironment(capture(slot)) } returns Unit

        subject.update(Environment.PreProd(graphQLUrl = PRE_PROD_URL, webUrl = WEB_URL))

        val result = slot.captured.invoke()

        assertEquals(EnvironmentProto.PRE_PROD, result)
    }

    @Test
    fun `update - given env prod then should map to proto prod`() = runTest {
        val slot = slot<() -> EnvironmentProto>()
        coEvery { debugPreferencesDataSource.updateEnvironment(capture(slot)) } returns Unit

        subject.update(Environment.Prod(graphQLUrl = PROD_URL, webUrl = WEB_URL))

        val result = slot.captured.invoke()

        assertEquals(EnvironmentProto.PROD, result)
    }

    @Test
    fun `update - given env custom then should map to proto custom and update the custom url`() = runTest {
        val slot = slot<() -> EnvironmentProto>()
        coEvery { debugPreferencesDataSource.updateEnvironment(capture(slot)) } returns Unit
        coEvery { debugPreferencesDataSource.updateCustomUrl(CUSTOM_URL) } returns Unit

        subject.update(Environment.Custom(graphQLUrl = CUSTOM_URL, webUrl = WEB_URL))

        val result = slot.captured.invoke()

        assertEquals(EnvironmentProto.CUSTOM, result)

        coVerify { debugPreferencesDataSource.updateCustomUrl(CUSTOM_URL) }
    }

    private fun createBuildConfiguration() = BuildConfiguration(
        appName = "Alfie",
        versionName = "123",
        applicationId = "123",
        debug = false,
        environments = Environments(
            dev = Environment.Dev(graphQLUrl = DEV_URL, webUrl = WEB_URL),
            preProd = Environment.PreProd(graphQLUrl = PRE_PROD_URL, webUrl = WEB_URL),
            prod = Environment.Prod(graphQLUrl = PROD_URL, webUrl = WEB_URL)
        )
    )
}
