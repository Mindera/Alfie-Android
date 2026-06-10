package com.mindera.alfie.data.search.repository

import app.cash.turbine.test
import com.mindera.alfie.data.database.search.RecentSearchDao
import com.mindera.alfie.data.search.listRecentSearch
import com.mindera.alfie.data.search.listRecentSearchEntity
import com.mindera.alfie.data.search.mapper.toDomain
import com.mindera.alfie.data.search.mapper.toEntity
import com.mindera.alfie.repository.search.model.RecentSearch
import io.mockk.coEvery
import io.mockk.coJustRun
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
internal class SearchRepositoryImplTest {

    @MockK
    private lateinit var recentSearchDao: RecentSearchDao

    @InjectMockKs
    private lateinit var subject: SearchRepositoryImpl

    @Test
    fun `WHEN getRecentSearches is called THEN get data via dao, map to domain and return it`() = runTest {
        coEvery { recentSearchDao.getRecentSearches() } returns flowOf(listRecentSearchEntity)
        val expected = listRecentSearch

        subject.getRecentSearches().test {
            val result = awaitItem()

            coVerify {
                recentSearchDao.getRecentSearches()
                listRecentSearchEntity.toDomain()
            }
            assertEquals(expected, result)

            awaitComplete()
        }
    }

    @Test
    fun `WHEN saveRecentSearch is called THEN map to entity and call dao, clear old searches`() = runTest {
        val recentSearchMock = mockk<RecentSearch>(relaxed = true)

        coJustRun { recentSearchDao.saveRecentSearch(any()) }
        coJustRun { recentSearchDao.clearOldSearches() }

        subject.saveRecentSearch(recentSearchMock)

        coVerify {
            recentSearchMock.toEntity()
            recentSearchDao.saveRecentSearch(any())
            recentSearchDao.clearOldSearches()
        }
    }

    @Test
    fun `WHEN deleteRecentSearch is called THEN call dao to delete recent search`() = runTest {
        val recentSearchMock = mockk<RecentSearch>(relaxed = true)

        coJustRun { recentSearchDao.deleteRecentSearch(any()) }

        subject.deleteRecentSearch(recentSearchMock)

        coVerify {
            recentSearchMock.toEntity()
            recentSearchDao.deleteRecentSearch(any())
        }
    }

    @Test
    fun `WHEN clearRecentSearches is called THEN call dao to delete all recent searches`() = runTest {
        coJustRun { recentSearchDao.clearRecentSearches() }

        subject.clearRecentSearches()

        coVerify { recentSearchDao.clearRecentSearches() }
    }
}
