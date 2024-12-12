package au.com.alfie.ecomm.debug.interceptor

import okhttp3.Interceptor

interface DebugInterceptors {

    operator fun invoke(): List<Interceptor>
}
