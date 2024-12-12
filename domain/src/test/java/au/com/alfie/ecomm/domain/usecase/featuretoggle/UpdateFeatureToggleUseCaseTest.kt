package au.com.alfie.ecomm.domain.usecase.featuretoggle

import au.com.alfie.ecomm.repository.featuretoggle.FeatureToggleRepository
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UpdateFeatureToggleUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: FeatureToggleRepository

    @InjectMockKs
    private lateinit var subject: UpdateFeatureToggleUseCase

    companion object {

        val mockFeatureToggle = mockk<FeatureToggle>()
    }

    @Test
    fun `Called updateFeatureToggle invoke`() = runTest {
        subject(mockFeatureToggle)

        coVerify { repository.updateFeatureToggle(mockFeatureToggle) }
    }
}