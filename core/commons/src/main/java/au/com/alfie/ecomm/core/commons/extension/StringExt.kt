package au.com.alfie.ecomm.core.commons.extension

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.text.Normalizer
import java.util.Locale
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun String.capitalize(): String =
    this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(Locale.getDefault())
        } else {
            it.toString()
        }
    }

fun String.capitalizeAllWorlds(): String {
    val capitalizeWords = this.lowercase().split(" ").map {
        it.capitalize()
    }
    return capitalizeWords.joinToString(" ")
}

fun String?.toIntOrZero(): Int = this.orEmpty().toIntOrNull() ?: 0

@OptIn(ExperimentalContracts::class)
fun String?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }
    return !this.isNullOrBlank()
}

inline fun <reified T> String.fromJson(): T? =
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(T::class.java)
        .fromJson(this)

/**
 * Performs a Canonical decomposition. This process separates the words with accents
 * into two new chars, one for the accent symbol and another one
 * for the word itself. Then it's a matter of applying a regex for filtering out
 * the accents and leaving just the words.
 */
fun String.removeAccents() =
    Normalizer.normalize(
        /* src = */ this,
        /* form = */ Normalizer.Form.NFD
    ).replace(
        regex = "\\p{Mn}+".toRegex(),
        replacement = ""
    )
