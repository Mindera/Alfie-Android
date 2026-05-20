package com.mindera.alfie.domain.usecase.navigation

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.navigation.NavigationRepository
import com.mindera.alfie.repository.navigation.model.HandleType.HEADER
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class GetRootNavEntriesUseCaseTest {

    @RelaxedMockK
    lateinit var navigationRepository: NavigationRepository

    @InjectMockKs
    lateinit var subject: GetRootNavEntriesUseCase

    @Test
    fun `invoke - WHEN repository result is success THEN result is success`() = runTest {
        val mockEntries = mockk<List<NavEntry>>()
        coEvery { navigationRepository.getNavEntriesByHandle(HEADER) } returns RepositoryResult.Success(mockEntries)

        val expected = UseCaseResult.Success(mockEntries)

        val result = subject()

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val mockError = mockk<ErrorResult>()
        coEvery { navigationRepository.getNavEntriesByHandle(HEADER) } returns RepositoryResult.Error(mockError)

        val expected = UseCaseResult.Error(mockError)

        val result = subject()

        assertEquals(expected, result)
    }
}
