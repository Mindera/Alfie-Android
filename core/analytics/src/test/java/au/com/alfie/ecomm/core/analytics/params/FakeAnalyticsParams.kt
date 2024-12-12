package au.com.alfie.ecomm.core.analytics.params

internal class FakeAnalyticsParams(
    private val params: Map<String, AnalyticsValues>? = null
) : AnalyticsParams {

    override fun params(): Map<String, AnalyticsValues> = params ?: mapOf("attribute" to AnalyticsValues.StringValues("value"))
}
