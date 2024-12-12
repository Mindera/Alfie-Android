package au.com.alfie.ecomm.debug.operational.view.featuretoggle

import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.usecase.featuretoggle.GetAllFeatureToggleUseCase
import au.com.alfie.ecomm.domain.usecase.featuretoggle.SaveFeatureToggleUseCase
import au.com.alfie.ecomm.domain.usecase.featuretoggle.UpdateFeatureToggleUseCase
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggleType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class FeatureToggleViewModelTest {

    @RelaxedMockK
    private lateinit var getAllFeatureToggle: GetAllFeatureToggleUseCase

    @RelaxedMockK
    private lateinit var saveFeatureToggle: SaveFeatureToggleUseCase

    @RelaxedMockK
    private lateinit var updateFeatureToggle: UpdateFeatureToggleUseCase

    @RelaxedMockK
    private lateinit var featureToggleUIFactory: FeatureToggleUIFactory

    private lateinit var viewModel: FeatureToggleViewModel

    @MockK
    private lateinit var mockFeatureToggleList: List<FeatureToggle>

    @MockK
    private lateinit var mockFeatureToggleFlow: Flow<List<FeatureToggle>>

    @MockK
    private lateinit var mockFeatureToggle: FeatureToggle

    @BeforeEach
    fun setUp() {
        coEvery { getAllFeatureToggle() } returns mockFeatureToggleFlow
    }

    @Test
    fun addFeatureToggle() = runTest {
        coEvery { featureToggleUIFactory.invoke() } returns mockFeatureToggleList

        viewModel = buildSubject()

        coVerify { saveFeatureToggle(mockFeatureToggleList) }
    }

    @SuppressWarnings("unused")
    private fun featureToggleParams() = arrayOf(
        FeatureToggle(toggleTitle = "Switch", enabled = true, type = FeatureToggleType.SWITCH.value),
        FeatureToggle(toggleTitle = "Switch", enabled = false, type = FeatureToggleType.SWITCH.value),
        FeatureToggle(toggleTitle = "Input", enabled = true, type = FeatureToggleType.INPUT.value),
        FeatureToggle(toggleTitle = "Input", enabled = false, type = FeatureToggleType.INPUT.value)
    )

    @ParameterizedTest
    @MethodSource("featureToggleParams")
    fun updateFeatureToggle(featureToggle: FeatureToggle) = runTest {
        viewModel = buildSubject()
        viewModel.updateFeatureToggle(featureToggle)

        coVerify { updateFeatureToggle.invoke(featureToggle) }
    }

    private fun buildSubject() = FeatureToggleViewModel(
        getAllFeatureToggleUseCase = getAllFeatureToggle,
        featureToggleUIFactory = featureToggleUIFactory,
        saveFeatureToggleUseCase = saveFeatureToggle,
        updateFeatureToggleUseCase = updateFeatureToggle
    )
}