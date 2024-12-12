package au.com.alfie.ecomm.core.commons.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    /**
     *  ISO 8601 date format
     */
    private const val DATE_PATTERN_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val DATE_PATTERN_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    /**
     *  Converts a date (yyyy-MM-dd'T'HH:mm:ss'Z')/ (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
     *  to date format passed by args
     *  and returns it in a String format
     *
     *  example: convertToDateFormat("1992-03-16T00:00:00Z", "EEEE dd MMMM")
     *  example: convertToDateFormat("1992-03-16T00:00:00.123Z", "EEEE dd MMMM")
     */
    fun convertToDateFormat(
        date: String,
        dateFormat: String,
        locale: Locale = Locale.ENGLISH
    ): String {
        val formatUtc = SimpleDateFormat(DATE_PATTERN_UTC, locale)
        val formatISO = SimpleDateFormat(DATE_PATTERN_ISO_8601, locale)

        val dateUtc = runCatching {
            formatUtc.parse(date)
        }.recoverCatching {
            formatISO.parse(date)
        }.getOrNull() ?: return date

        val formatLocal = SimpleDateFormat(dateFormat, locale)

        return formatLocal.format(dateUtc)
    }

    /**
     *  Converts a date (Long) in milliseconds to date format passed by args
     *  and returns it in a String format
     *
     *  example: convertHourToDateFormat("1706054400000", "dd/MM/YYYY")
     */

    fun convertMsToDate(
        date: Long,
        dateFormat: String
    ): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return formatter.format(calendar.time)
    }
}
