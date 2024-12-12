package au.com.alfie.ecomm.core.commons.util

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DateUtilsTest {

    private val datePatternUTC = "1992-03-16T00:00:00Z"
    private val datePatternISO = "1992-03-16T00:00:00.123Z"
    private val datePatternMilliseconds = 1706054400000L

    @Test
    fun shouldConvertToMonthDayYear() {
        val dateFormat = "MM/dd/yyyy"
        val dateFormatted = "03/16/1992"

        // Then
        shouldConvertToDateFormat(datePatternUTC, dateFormat, dateFormatted)
    }

    @Test
    fun shouldConvertToDayFullMonthYear() {
        val dateFormat = "dd MMMM yyyy"
        val dateFormatted = "16 March 1992"

        // Then
        shouldConvertToDateFormat(datePatternUTC, dateFormat, dateFormatted)
    }

    @Test
    fun shouldConvertToDayWeekDayNumberFullMonth() {
        val dateFormat = "EEEE dd MMMM"
        val dateFormatted = "Monday 16 March"

        // Then
        shouldConvertToDateFormat(datePatternISO, dateFormat, dateFormatted)
    }

    @Test
    fun shouldConvertToDayMonthFullYear() {
        val dateFormat = "dd/MM/YYYY"
        val dateFormatted = "24/01/2024"

        // Then
        val date = DateUtils.convertMsToDate(datePatternMilliseconds, dateFormat)
        assertEquals(dateFormatted, date)
    }

    private fun shouldConvertToDateFormat(datePattern: String, dateFormat: String, dateResult: String) {
        assertEquals(
            DateUtils.convertToDateFormat(datePattern, dateFormat),
            dateResult
        )
    }
}
