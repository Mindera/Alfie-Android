package au.com.alfie.ecomm.debug.operational.view.featuretoggle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.domain.usecase.featuretoggle.GetAllFeatureToggleUseCase
import au.com.alfie.ecomm.domain.usecase.featuretoggle.SaveFeatureToggleUseCase
import au.com.alfie.ecomm.domain.usecase.featuretoggle.UpdateFeatureToggleUseCase
import au.com.alfie.ecomm.repository.featuretoggle.model.FeatureToggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeatureToggleViewModel @Inject constructor(
    getAllFeatureToggleUseCase: GetAllFeatureToggleUseCase,
    private val featureToggleUIFactory: FeatureToggleUIFactory,
    private val saveFeatureToggleUseCase: SaveFeatureToggleUseCase,
    private val updateFeatureToggleUseCase: UpdateFeatureToggleUseCase
) : ViewModel() {

    val featureToggle = getAllFeatureToggleUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            saveFeatureToggleUseCase(featureToggleUIFactory())
        }
    }

    fun updateFeatureToggle(featureToggle: FeatureToggle) {
        viewModelScope.launch {
            updateFeatureToggleUseCase(featureToggle)
        }
    }
}
