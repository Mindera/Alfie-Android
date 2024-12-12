package au.com.alfie.ecomm.domain.usecase.featuretoggle

import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
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

@ExtendWith(MockKExtension::class)
class GetAllFeatureToggleUseCaseTest {

    @MockK
    private lateinit var repository: FeatureToggleRepository

    @InjectMockKs
    private lateinit var subject: GetAllFeatureToggleUseCase

    companion object {
        val mockFeatureToggles = flowOf(
            listOf(
                mockk<FeatureToggle>(),
                mockk<FeatureToggle>(),
                mockk<FeatureToggle>()
            )
        )
    }

    @Test
    fun `Called getAllFeatureToggle invoke then flow is returned`() = runTest {
        coEvery { repository.getAllFeatureTogglesAsFlow() } returns mockFeatureToggles

        val result = subject()

        coVerify { repository.getAllFeatureTogglesAsFlow() }
        kotlin.test.assertEquals(mockFeatureToggles, result)
    }
}