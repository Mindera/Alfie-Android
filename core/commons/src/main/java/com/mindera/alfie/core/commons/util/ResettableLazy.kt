package com.mindera.alfie.core.commons.util

interface ResettableLazy<out T> : Lazy<T> {

    fun reset()
}
