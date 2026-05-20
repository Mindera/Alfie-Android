package com.mindera.alfie.feature.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.feature.startup.loader.StartUpLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class StartUpViewModel @Inject constructor(
    private val loaders: Set<@JvmSuppressWildcards StartUpLoader>
) : ViewModel() {

    private val _startDestination: MutableStateFlow<Screen?> = MutableStateFlow(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val loadedSuccessfully = loaders.map { async { it.load() } }
                .awaitAll()
                .all { it.isSuccess }

            if (loadedSuccessfully) {
                // TODO Add onboarding condition when adding the onboarding screen
                _startDestination.value = Screen.Home
            } else {
                // TODO Error page?
                _startDestination.value = null
            }
        }
    }
}
