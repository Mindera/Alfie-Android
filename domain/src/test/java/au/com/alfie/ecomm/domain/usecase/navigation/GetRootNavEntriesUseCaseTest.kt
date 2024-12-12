package au.com.alfie.ecomm.domain.usecase.navigation

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.navigation.NavigationRepository
import au.com.alfie.ecomm.repository.navigation.model.HandleType.HEADER
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.result.ErrorResult
import au.com.alfie.ecomm.repository.result.RepositoryResult
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
