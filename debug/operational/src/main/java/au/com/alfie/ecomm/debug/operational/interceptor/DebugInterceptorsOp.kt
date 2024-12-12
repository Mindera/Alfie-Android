package au.com.alfie.ecomm.debug.operational.interceptor

import au.com.alfie.ecomm.debug.interceptor.DebugInterceptors
import okhttp3.Interceptor
import javax.inject.Inject

internal class DebugInterceptorsOp @Inject constructor(
    private val chuckerCreator: ChuckerCreator
) : DebugInterceptors {

    override operator fun invoke(): List<Interceptor> = listOf(
        chuckerCreator.create()
    )
}
