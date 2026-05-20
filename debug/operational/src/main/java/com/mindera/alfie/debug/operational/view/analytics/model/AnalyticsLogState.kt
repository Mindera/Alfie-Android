package com.mindera.alfie.debug.operational.view.analytics.model

internal sealed interface AnalyticsLogState {

    data object Empty : AnalyticsLogState

    data class Data(val analyticsLogUI: AnalyticsLogUI) : AnalyticsLogState
}
