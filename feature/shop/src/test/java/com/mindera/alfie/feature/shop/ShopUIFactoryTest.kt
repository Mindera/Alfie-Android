package com.mindera.alfie.feature.shop

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.feature.shop.ShopUIFactory.Companion.SERVICES_WEB_URL
import com.mindera.alfie.feature.shop.model.ShopUI
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class ShopUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @RelaxedMockK
    private lateinit var environmentManager: EnvironmentManager

    @InjectMockKs
    private lateinit var uiFactory: ShopUIFactory

    @BeforeEach
    fun setup() {
        every { dispatcherProvider.default() } returns Dispatchers.Default
        coEvery { environmentManager.current() } returns Environment.Prod(graphQLUrl = "", webUrl = BASE_URL)
    }

    @Test
    fun `invoke - returns the shop entries`() = runTest {
        val expected = ShopUI(servicesUrl = "$BASE_URL/$SERVICES_WEB_URL")

        val result = uiFactory()

        assertEquals(expected, result)
    }
}
