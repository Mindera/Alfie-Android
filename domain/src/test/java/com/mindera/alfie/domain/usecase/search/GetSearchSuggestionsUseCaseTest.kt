package com.mindera.alfie.domain.usecase.search

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.search.SearchRepository
import com.mindera.alfie.repository.search.model.SearchSuggestions
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
class GetSearchSuggestionsUseCaseTest {

    @RelaxedMockK
    lateinit var searchRepository: SearchRepository

    @InjectMockKs
    lateinit var useCase: GetSearchSuggestionsUseCase

    @Test
    fun `invoke - WHEN repository result is success THEN result is success`() = runTest {
        val searchSuggestions = mockk<SearchSuggestions>()
        val expected = UseCaseResult.Success(searchSuggestions)

        coEvery { searchRepository.getSearchSuggestions(any()) } returns RepositoryResult.Success(searchSuggestions)

        val result = useCase(query = "query")

        assertEquals(expected, result)
    }

    @Test
    fun `invoke - WHEN repository result is error THEN result is error`() = runTest {
        val errorResult = mockk<ErrorResult>()
        val expected = UseCaseResult.Error(errorResult)

        coEvery { searchRepository.getSearchSuggestions(any()) } returns RepositoryResult.Error(errorResult)

        val result = useCase(query = "query")

        assertEquals(expected, result)
    }
}
