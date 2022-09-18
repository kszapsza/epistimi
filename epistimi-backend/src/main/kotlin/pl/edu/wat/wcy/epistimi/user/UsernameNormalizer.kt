package pl.edu.wat.wcy.epistimi.user

import java.text.Normalizer
import java.util.Locale
import java.util.regex.Pattern

object UsernameNormalizer {
    private val DIACRITICS_PATTERN by lazy { Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+") }

    fun normalize(username: String): String {
        return username
            .lowercase(Locale.getDefault())
            .filterDotLetterOrDigit()
            .removeDiacritics()
    }

    private fun String.filterDotLetterOrDigit(): String {
        return this.filter { char -> char == '.' || char.isLetterOrDigit() }
    }

    private fun String.removeDiacritics(): String {
        return this
            .let { Normalizer.normalize(it, Normalizer.Form.NFD) }
            .let { DIACRITICS_PATTERN.matcher(it).replaceAll("") }
            .replace("ł", "l")

    }
}
