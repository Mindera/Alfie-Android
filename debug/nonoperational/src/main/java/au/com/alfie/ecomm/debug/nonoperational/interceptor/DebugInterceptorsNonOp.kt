package au.com.alfie.ecomm.debug.nonoperational.interceptor

import au.com.alfie.ecomm.debug.interceptor.DebugInterceptors
import okhttp3.Interceptor
import javax.inject.Inject

internal class DebugInterceptorsNonOp @Inject constructor() : DebugInterceptors {

    override operator fun invoke(): List<Interceptor> = emptyList()
}
