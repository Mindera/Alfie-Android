package au.com.alfie.ecomm.feature.bag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.domain.doOnResult
import au.com.alfie.ecomm.domain.usecase.bag.GetBagUseCase
import au.com.alfie.ecomm.feature.bag.BagUiState.Data.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BagViewModel @Inject constructor(
    private val getBagUseCase: GetBagUseCase,
    private val bagUiFactory: BagUiFactory,
) : ViewModel() {

    private val _state = MutableStateFlow<BagUiState>(Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getBagList()
        }
    }

    private suspend fun getBagList() {
        getBagUseCase().doOnResult(
            onSuccess = {
                _state.value = BagUiState.Data.Loaded(
                    bagUiFactory(
                        products = it
                    )
                )
            },
            onError = {
                _state.value = BagUiState.Error
            }
        )
    }
}