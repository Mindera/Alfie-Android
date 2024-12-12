package au.com.alfie.ecomm.domain.usecase.search

import au.com.alfie.ecomm.repository.search.SearchRepository
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class GetRecentSearchesUseCaseTest {

    @MockK
    private lateinit var repository: SearchRepository

    @InjectMockKs
    private lateinit var subject: GetRecentSearchesUseCase

    companion object {

        val mockRecentSearches = flowOf(
            listOf(
                mockk<RecentSearch>(),
                mockk<RecentSearch>(),
                mockk<RecentSearch>()
            )
        )
    }

    @Test
    fun `WHEN use case is invoked THEN flow is returned`() = runTest {
        coEvery { repository.getRecentSearches() } returns mockRecentSearches

        val result = subject()

        coVerify { repository.getRecentSearches() }
        assertEquals(mockRecentSearches, result)
    }
}
