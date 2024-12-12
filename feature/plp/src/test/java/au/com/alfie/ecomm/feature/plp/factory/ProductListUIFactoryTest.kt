package au.com.alfie.ecomm.feature.plp.factory

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.feature.plp.model.ProductListUI
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import au.com.alfie.ecomm.repository.productlist.model.ProductListMetadata
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
import kotlin.test.assertFalse

@ExtendWith(MockKExtension::class)
class ProductListUIFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcherProvider: DispatcherProvider

    @InjectMockKs
    private lateinit var uiFactory: ProductListUIFactory

    @BeforeEach
    fun setUp() {
        every { dispatcherProvider.default() } returns Dispatchers.Default
    }

    @Test
    fun `invoke - WHEN layout mode is GRID THEN correctly defines the number of columns`() = runTest {
        val result = uiFactory(
            productListUI = ProductListUI.EMPTY,
            layoutMode = ProductListLayoutMode.GRID
        )

        assertEquals(ProductListLayoutMode.GRID, result.layoutMode)
        assertEquals(2, result.compactColumnCount)
        assertEquals(3, result.nonCompactColumnCount)
    }

    @Test
    fun `invoke - WHEN layout mode is COLUMN THEN correctly defines the number of columns`() = runTest {
        val result = uiFactory(
            productListUI = ProductListUI.EMPTY,
            layoutMode = ProductListLayoutMode.COLUMN
        )

        assertEquals(ProductListLayoutMode.COLUMN, result.layoutMode)
        assertEquals(1, result.compactColumnCount)
        assertEquals(2, result.nonCompactColumnCount)
    }

    @Test
    fun `invoke - WHEN metadata is provided THEN correctly maps the information`() = runTest {
        val metadata = ProductListMetadata(
            title = "Women",
            totalResults = 15,
            hierarchy = emptyList()
        )

        val result = uiFactory(
            productListUI = ProductListUI.EMPTY,
            metadata = metadata
        )

        assertEquals("Women", result.title)
        assertEquals(15, result.resultCount)
        assertFalse(result.isLoadingMetadata)
    }
}
