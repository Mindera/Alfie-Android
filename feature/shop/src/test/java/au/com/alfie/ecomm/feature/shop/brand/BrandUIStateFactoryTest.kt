package au.com.alfie.ecomm.feature.shop.brand

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.feature.shop.brand.factory.BrandUIStateFactory
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.brand.model.BrandUIState
import au.com.alfie.ecomm.feature.shop.brandUiState
import au.com.alfie.ecomm.feature.shop.brands
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class BrandUIStateFactoryTest {

    @RelaxedMockK
    private lateinit var dispatcher: DispatcherProvider

    @InjectMockKs
    private lateinit var subject: BrandUIStateFactory

    @BeforeEach
    fun setup() {
        coEvery { dispatcher.io() } returns Dispatchers.Default
    }

    @Test
    fun `invoke - returns BrandUIState`() = runTest {
        val expected = brandUiState
        val result = subject(brands)

        assertEquals(expected, result)
    }

    @Test
    fun `WHEN search term is filled THEN state is returned with a filtered list`() = runTest {
        val searchTerm = "idas"
        val expected = BrandUIState.Data(
            entries = persistentListOf(
                BrandEntryUI.Divider(character = 'A'),
                BrandEntryUI.Entry(
                    id = "123",
                    name = "Adidas",
                    slug = "adidas"
                ),
                BrandEntryUI.Entry(
                    id = "234",
                    name = "Ardidas",
                    slug = "ardidas"
                ),
                BrandEntryUI.Divider(character = 'B'),
                BrandEntryUI.Entry(
                    id = "456",
                    name = "Batidas",
                    slug = "batidas"
                )
            ),
            isLoading = false
        )
        val result = subject.filterBySearchTerm(
            entries = brandUiState.entries,
            searchTerm = searchTerm
        )
        assertEquals(expected, result)
    }

    @Test
    fun `WHEN search term is empty THEN state is returned with the same entries`() = runTest {
        val searchTerm = ""
        val expected = brandUiState
        val result = subject.filterBySearchTerm(
            entries = brandUiState.entries,
            searchTerm = searchTerm
        )
        assertEquals(expected, result)
    }
}
