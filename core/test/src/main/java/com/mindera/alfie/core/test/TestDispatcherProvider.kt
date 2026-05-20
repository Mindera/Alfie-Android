package com.mindera.alfie.core.test

import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {

    override fun default(): CoroutineDispatcher = Dispatchers.Main

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun io(): CoroutineDispatcher = Dispatchers.Main
}
