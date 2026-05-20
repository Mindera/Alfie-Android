package com.mindera.alfie.core.commons.extension

inline fun <T, R> Iterable<T>.firstPredicateOrNull(predicate: (T) -> R?): R? {
    return firstOrNull { predicate(it) != null }?.let { predicate(it) }
}

inline fun <T, R> Iterable<T>.firstPredicateOrDefault(predicate: (T) -> R?, default: () -> R): R {
    return firstPredicateOrNull { predicate(it) } ?: default.invoke()
}
