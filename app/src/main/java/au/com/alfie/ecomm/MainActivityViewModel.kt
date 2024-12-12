package au.com.alfie.ecomm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.debug.operational.R
import au.com.alfie.ecomm.domain.usecase.featuretoggle.GetFeatureToggleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getFeatureToggleUseCase: GetFeatureToggleUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _wishlistToggle: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val wishlistToggle: StateFlow<Boolean> = _wishlistToggle

    init {
        viewModelScope.launch {
            getFeatureToggleUseCase(
                context.getString(R.string.feature_toggle_show_wishlist)
            ).collect {
                _wishlistToggle.value = if (it.isEmpty()) {
                    false
                } else {
                    it[0].enabled
                }
            }
        }
    }
}