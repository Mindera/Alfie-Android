package au.com.alfie.ecomm.debug.operational.view.logcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_LINES = 100

@HiltViewModel
internal class LogcatViewModel @Inject constructor(
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _log = MutableStateFlow(emptyList<String>())
    val log: StateFlow<List<String>> = _log

    init {
        getLog()
    }

    private fun getLog() {
        viewModelScope.launch(context = dispatcher.io()) {
            Runtime.getRuntime().exec("logcat -d")
                .inputStream
                .bufferedReader()
                .useLines { lines ->
                    val output = lines.toList()
                        .takeLast(LOG_LINES)
                        .asReversed()
                    _log.value = output
                }
        }
    }
}
