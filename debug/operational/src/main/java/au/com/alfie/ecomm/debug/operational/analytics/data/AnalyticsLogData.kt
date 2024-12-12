package au.com.alfie.ecomm.debug.operational.analytics.data

internal data class AnalyticsLogData(
    val tracker: String,
    val event: String,
    val params: Map<String, String>,
    val timestamp: String
) {

    override fun toString(): String {
        return "Tracker: $tracker" +
            "\nEvent: $event" +
            "\nParams: $params"
    }
}
