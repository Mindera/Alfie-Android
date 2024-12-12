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
class SaveFeatureToggleUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: FeatureToggleRepository

    @InjectMockKs
    private lateinit var subject: SaveFeatureToggleUseCase

    companion object {
        val mockFeatureToggles =
            listOf(
                mockk<FeatureToggle>(),
                mockk<FeatureToggle>(),
                mockk<FeatureToggle>()
            )
    }

    @Test
    fun `Called saveFeatureToggle invoke then FeatureToggle list saved`() = runTest {
        subject(mockFeatureToggles)

        coVerify { repository.saveFeatureToggle(mockFeatureToggles) }
    }
}