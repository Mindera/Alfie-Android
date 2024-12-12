package au.com.alfie.ecomm.core.commons.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun default(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
}
