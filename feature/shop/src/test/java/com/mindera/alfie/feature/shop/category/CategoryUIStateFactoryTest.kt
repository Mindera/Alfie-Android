package com.mindera.alfie.feature.shop.category

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.feature.shop.category.factory.CategoryUIStateFactory
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.feature.shop.navEntries
import com.mindera.alfie.feature.shop.shopEntries
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class CategoryUIStateFactoryTest {

    @MockK
    private lateinit var dispatcher: DispatcherProvider

    @InjectMockKs
    private lateinit var uiFactory: CategoryUIStateFactory

    @BeforeEach
    fun setup() {
        coEvery { dispatcher.default() } returns Dispatchers.Default
    }

    @Test
    fun `invoke - returns CategoryUIState`() = runTest {
        val titleStringResource = StringResource.fromText("Title")
        val expected = CategoryUIState.Data(
            title = titleStringResource,
            entries = shopEntries.toImmutableList(),
            isLoading = false
        )

        val result = uiFactory(
            title = titleStringResource,
            navEntries = navEntries
        )

        assertEquals(expected, result)
    }
}
