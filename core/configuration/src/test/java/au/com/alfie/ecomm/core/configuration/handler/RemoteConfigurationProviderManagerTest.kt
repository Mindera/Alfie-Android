package au.com.alfie.ecomm.core.configuration.handler

import au.com.alfie.ecomm.core.configuration.RemoteConfigurationKey.ForceUpdate
import au.com.alfie.ecomm.core.configuration.Version
import au.com.alfie.ecomm.core.configuration.dto.ConfigurationData
import au.com.alfie.ecomm.core.configuration.model.RemoteConfiguration.VersionConfiguration
import au.com.alfie.ecomm.core.configuration.provider.FirebaseProvider
import au.com.alfie.ecomm.core.environment.model.BuildConfiguration
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class RemoteConfigurationProviderManagerTest {

    @MockK
    private lateinit var firebaseProvider: FirebaseProvider

    @RelaxedMockK
    private lateinit var buildConfiguration: BuildConfiguration

    @MockK
    private lateinit var mockConfigurationData: ConfigurationData

    @InjectMockKs
    private lateinit var subject: RemoteConfigurationManager

    @BeforeEach
    fun setup() {
        every { firebaseProvider.getConfig(ForceUpdate.value) } returns mockConfigurationData
    }

    @Test
    fun `WHEN get Version remote configuration GIVEN app version is the same THEN is disabled`() = runTest {
        every { buildConfiguration.versionName } returns "0.9.1"
        every { mockConfigurationData.version } returns "0.9.1"

        val forceUpdateConfig = subject.get<VersionConfiguration>(ForceUpdate)
        assertNotNull(forceUpdateConfig)
        assertFalse(forceUpdateConfig.isEnabled)
        assertEquals(forceUpdateConfig.version, Version("0.9.1"))
    }

    @Test
    fun `WHEN get Version remote configuration GIVEN app version is older THEN is enabled`() = runTest {
        every { buildConfiguration.versionName } returns "0.9.1"
        every { mockConfigurationData.version } returns "0.9.2"

        val forceUpdateConfig = subject.get<VersionConfiguration>(ForceUpdate)
        assertNotNull(forceUpdateConfig)
        assertTrue(forceUpdateConfig.isEnabled)
        assertEquals(forceUpdateConfig.version, Version("0.9.2"))
    }

    @Test
    fun `WHEN get Version remote configuration GIVEN app version is newer THEN is disabled`() = runTest {
        every { buildConfiguration.versionName } returns "0.9.2"
        every { mockConfigurationData.version } returns "0.9.1"

        val forceUpdateConfig = subject.get<VersionConfiguration>(ForceUpdate)
        assertNotNull(forceUpdateConfig)
        assertFalse(forceUpdateConfig.isEnabled)
        assertEquals(forceUpdateConfig.version, Version("0.9.1"))
    }
}
