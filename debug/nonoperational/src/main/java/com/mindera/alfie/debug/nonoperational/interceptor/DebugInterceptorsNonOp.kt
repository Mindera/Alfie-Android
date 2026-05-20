package com.mindera.alfie.debug.nonoperational.interceptor

import com.mindera.alfie.debug.interceptor.DebugInterceptors
import okhttp3.Interceptor
import javax.inject.Inject

internal class DebugInterceptorsNonOp @Inject constructor() : DebugInterceptors {

    override operator fun invoke(): List<Interceptor> = emptyList()
}
