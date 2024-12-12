package au.com.alfie.ecomm.domain.usecase.search

import au.com.alfie.ecomm.repository.search.SearchRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ClearRecentSearchesUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: SearchRepository

    @InjectMockKs
    private lateinit var subject: ClearRecentSearchesUseCase

    @Test
    fun `WHEN use case is invoked THEN repository should be called`() = runTest {
        subject()
        coVerify { repository.clearRecentSearches() }
    }
}
