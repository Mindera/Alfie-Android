package com.mindera.alfie.domain.usecase.search

import com.mindera.alfie.repository.search.SearchRepository
import com.mindera.alfie.repository.search.model.RecentSearch
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class SaveRecentSearchUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: SearchRepository

    @InjectMockKs
    private lateinit var subject: SaveRecentSearchUseCase

    @Test
    fun `WHEN use case is invoked THEN repository should be called`() = runTest {
        val recentSearch = mockk<RecentSearch>()

        subject(recentSearch)

        coVerify { repository.saveRecentSearch(recentSearch) }
    }
}
