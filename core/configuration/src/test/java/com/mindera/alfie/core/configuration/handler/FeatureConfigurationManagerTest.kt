package com.mindera.alfie.core.configuration.handler

import com.mindera.alfie.core.configuration.FeatureKey
import com.mindera.alfie.core.configuration.dto.FeatureData
import com.mindera.alfie.core.configuration.model.FeatureConfiguration
import com.mindera.alfie.core.configuration.provider.ConfigurationProvider
import com.mindera.alfie.core.environment.model.BuildConfiguration
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class FeatureConfigurationManagerTest {

    @MockK
    private lateinit var configurationProvider: ConfigurationProvider

    @MockK
    private lateinit var buildConfiguration: BuildConfiguration

    @RelaxedMockK
    private lateinit var mockFeatureData: FeatureData

    @RelaxedMockK
    private lateinit var mockFeatureConfiguration: FeatureConfiguration

    @InjectMockKs
    private lateinit var subject: FeatureConfigurationManager

    @Test
    fun `WHEN get Boolean feature configuration GIVEN it is true THEN feature is enabled`() = runTest {
        every { configurationProvider.getBoolean(any()) } returns true
        val result = subject.isEnabled(FeatureKey.TestFeature1)
        assertTrue(result)
    }

    @Test
    fun `WHEN get Boolean feature configuration GIVEN it is false THEN feature is disabled`() = runTest {
        every { configurationProvider.getBoolean(any()) } returns false
        val result = subject.isEnabled(FeatureKey.TestFeature1)
        assertFalse(result)
    }
}
