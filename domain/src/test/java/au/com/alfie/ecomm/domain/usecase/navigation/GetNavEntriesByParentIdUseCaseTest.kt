package au.com.alfie.ecomm.domain.usecase.navigation

import au.com.alfie.ecomm.repository.navigation.NavigationRepository
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
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
internal class GetNavEntriesByParentIdUseCaseTest {

    @RelaxedMockK
    lateinit var navigationRepository: NavigationRepository

    @InjectMockKs
    lateinit var subject: GetNavEntriesByParentIdUseCase

    @Test
    fun `invoke - returns cached nav entries by parentId`() = runTest {
        val expected = mockk<List<NavEntry>>()

        coEvery { navigationRepository.getByParentId(any()) } returns expected

        val result = subject(parentId = 123)

        assertEquals(expected, result)
    }
}
