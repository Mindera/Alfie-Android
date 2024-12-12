package au.com.alfie.ecomm.debug.operational.view.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogData
import au.com.alfie.ecomm.debug.operational.analytics.data.AnalyticsLogDataGetter
import au.com.alfie.ecomm.debug.operational.view.analytics.model.AnalyticsLogState
import au.com.alfie.ecomm.debug.operational.view.analytics.model.AnalyticsLogUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ALL_EVENTS = "All"

@HiltViewModel
internal class AnalyticsLogViewModel @Inject constructor(
    private val analyticsLogDataGetter: AnalyticsLogDataGetter,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _state: MutableStateFlow<AnalyticsLogState> = MutableStateFlow(AnalyticsLogState.Empty)
    val state: StateFlow<AnalyticsLogState> = _state

    init {
        viewModelScope.launch(dispatcherProvider.io()) {
            val result = analyticsLogDataGetter.get()
            if (result.isEmpty()) {
                _state.value = AnalyticsLogState.Empty
            } else {
                _state.value = allEventsState(result)
            }
        }
    }

    fun filterByTracker(tracker: String) {
        viewModelScope.launch(dispatcherProvider.io()) {
            when (tracker) {
                ALL_EVENTS -> _state.value = allEventsState(analyticsLogDataGetter.get())
                else -> _state.value.withDataState {
                    _state.value = filterEvents(it, tracker)
                }
            }
        }
    }

    private fun allEventsState(result: List<AnalyticsLogData>) = AnalyticsLogState.Data(
        AnalyticsLogUI(
            trackers = setOf(ALL_EVENTS) + result.groupBy { it.tracker }.keys,
            events = result
        )
    )

    private fun filterEvents(
        ui: AnalyticsLogUI,
        tracker: String
    ) = AnalyticsLogState.Data(
        analyticsLogUI = ui.copy(
            events = analyticsLogDataGetter.get().filter { it.tracker == tracker }
        )
    )

    private fun <T> AnalyticsLogState.withDataState(block: (AnalyticsLogUI) -> T) {
        if (this is AnalyticsLogState.Data) {
            block(analyticsLogUI)
        }
    }
}
