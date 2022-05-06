package pl.edu.wat.wcy.epistimi

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

internal object TestUtils {
    fun parseDate(text: String): LocalDate {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
            .let { formatter -> LocalDate.parse(text, formatter) }
    }
}
