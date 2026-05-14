package com.mindera.alfie.debug.operational.view.environment

import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.core.environment.model.Environments
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.core.test.TestDispatcherProvider
import com.mindera.alfie.debug.operational.R
import com.mindera.alfie.debug.operational.view.environment.model.EnvironmentUI
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class EnvironmentUIFactoryTest {

    companion object {
        private const val BASE_URL = "www.alfie.com"
    }

    private val environmentManager: EnvironmentManager = mockk()

    private val testDispatcherProvider = TestDispatcherProvider()

    private val subject: EnvironmentUIFactory = EnvironmentUIFactory(environmentManager, testDispatcherProvider)

    @Test
    fun `invoke - then get all the environments`() = runTest {
        val dev = Environment.Dev("devUrl", BASE_URL)
        val preProd = Environment.PreProd("preProdUrl", BASE_URL)
        val prod = Environment.Prod("prodUrl", BASE_URL)
        val custom = Environment.Custom("customUrl", BASE_URL)
        coEvery { environmentManager.current() } returns dev
        coEvery { environmentManager.environments() } returns Environments(
            dev = dev,
            preProd = preProd,
            prod = prod
        )
        coEvery { environmentManager.custom() } returns custom

        val expected = listOf(
            EnvironmentUI(
                environment = dev,
                isEnabled = true,
                isSelected = true,
                enableUrlChange = false,
                label = StringResource.fromId(R.string.environment_dev)
            ),
            EnvironmentUI(
                environment = preProd,
                isEnabled = false,
                isSelected = false,
                enableUrlChange = false,
                label = StringResource.fromId(R.string.environment_pre_prod)
            ),
            EnvironmentUI(
                environment = prod,
                isEnabled = false,
                isSelected = false,
                enableUrlChange = false,
                label = StringResource.fromId(R.string.environment_prod)
            ),
            EnvironmentUI(
                environment = custom,
                isEnabled = true,
                isSelected = false,
                enableUrlChange = true,
                label = StringResource.fromId(R.string.environment_custom)
            )
        )

        val result = subject.invoke()

        assertEquals(expected, result)
    }
}
