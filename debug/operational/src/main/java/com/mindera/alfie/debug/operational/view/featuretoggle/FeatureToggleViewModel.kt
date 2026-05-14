package com.mindera.alfie.debug.operational.view.featuretoggle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.domain.usecase.featuretoggle.GetAllFeatureToggleUseCase
import com.mindera.alfie.domain.usecase.featuretoggle.SaveFeatureToggleUseCase
import com.mindera.alfie.domain.usecase.featuretoggle.UpdateFeatureToggleUseCase
import com.mindera.alfie.repository.featuretoggle.model.FeatureToggle
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
