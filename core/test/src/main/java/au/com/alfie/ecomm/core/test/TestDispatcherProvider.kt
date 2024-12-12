package au.com.alfie.ecomm.core.test

import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {

    override fun default(): CoroutineDispatcher = Dispatchers.Main

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun io(): CoroutineDispatcher = Dispatchers.Main
}
