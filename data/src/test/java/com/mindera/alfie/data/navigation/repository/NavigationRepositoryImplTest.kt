package com.mindera.alfie.data.navigation.repository

import com.mindera.alfie.data.database.navigation.NavigationEntryDao
import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity
import com.mindera.alfie.data.navigation.cache.mapper.toDomain
import com.mindera.alfie.data.navigation.navEntriesData
import com.mindera.alfie.data.navigation.navEntryEntities
import com.mindera.alfie.data.navigation.remote.service.RemoteNavigationService
import com.mindera.alfie.network.exception.GraphNetworkException
import com.mindera.alfie.repository.navigation.model.HandleType
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.navigation.model.NavItemType
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class NavigationRepositoryImplTest {

    @RelaxedMockK
    private lateinit var remoteNavigationService: RemoteNavigationService

    @RelaxedMockK
    private lateinit var navigationEntryDao: NavigationEntryDao

    @InjectMockKs
    lateinit var subject: NavigationRepositoryImpl

    @Test
    fun `WHEN getByParentId THEN should call getByParentId in DAO and map the result`() = runTest {
        val parentId = 123
        val entitiesFromDao = listOf(
            NavigationEntryEntity(id = 1, title = "Entry 1", parentId = parentId, navItemType = "HOME", path = "url"),
            NavigationEntryEntity(id = 2, title = "Entry 2", parentId = parentId, navItemType = "HOME", path = "url")
        )
        val expectedEntries = entitiesFromDao.toDomain()

        coEvery { navigationEntryDao.getByParentId(parentId) } returns entitiesFromDao

        val result = subject.getByParentId(parentId = parentId)

        coVerify { navigationEntryDao.getByParentId(parentId) }
        Assertions.assertEquals(expectedEntries, result)
    }

    @Test
    fun `GIVEN getNavEntriesByHandle WHEN result is success THEN is mapped to repository result`() = runTest {
        val expected = RepositoryResult.Success(
            listOf(
                NavEntry(
                    id = 1,
                    title = "Home Item",
                    url = "https://home.item",
                    type = NavItemType.HOME
                ),
                NavEntry(
                    id = 2,
                    title = "Product Item",
                    url = "https://product.item",
                    type = NavItemType.PRODUCT
                )
            )
        )
        coEvery { remoteNavigationService.getNavEntriesByHandle(any()) } returns Result.success(navEntriesData)
        coEvery { navigationEntryDao.insert(any()) } returns navEntryEntities

        val result = subject.getNavEntriesByHandle(handleType = HandleType.HEADER)

        assertEquals(expected, result)
    }

    @Test
    fun `GIVEN getNavEntriesByHandle WHEN result is failure THEN exception is wrapped in error result`() = runTest {
        val expected = RepositoryResult.Error(
            ErrorResult(
                type = ErrorType.UNKNOWN,
                errorMessage = "Error",
                code = "0"
            )
        )
        coEvery { remoteNavigationService.getNavEntriesByHandle(any()) } returns Result.failure(
            GraphNetworkException.UnexpectedException(message = "Error")
        )

        val result = subject.getNavEntriesByHandle(handleType = HandleType.HEADER)

        assertEquals(expected, result)
    }
}
