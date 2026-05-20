package com.mindera.alfie.domain.usecase.brand

import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.brand.BrandRepository
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.shared.model.Brand
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
internal class GetBrandsUseCaseTest {

    @RelaxedMockK
    private lateinit var brandRepository: BrandRepository

    @InjectMockKs
    lateinit var subject: GetBrandsUseCase

    @Test
    fun `invoke - returns list of brands`() = runTest {
        val mockBrands = mockk<List<Brand>>()
        coEvery { brandRepository.getBrands() } returns RepositoryResult.Success(mockBrands)

        val expected = UseCaseResult.Success(mockBrands)

        val result = subject()

        assertEquals(expected, result)
    }
}
