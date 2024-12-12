package au.com.alfie.ecomm.core.analytics.params

sealed class AnalyticsValues {

    data class IntValues(val int: Int) : AnalyticsValues() {

        override fun toString(): String = "$int"
    }

    data class StringValues(val string: String) : AnalyticsValues() {

        override fun toString(): String = string
    }
}
