package com.mindera.alfie.debug.operational.interceptor

import com.mindera.alfie.debug.interceptor.DebugInterceptors
import okhttp3.Interceptor
import javax.inject.Inject

internal class DebugInterceptorsOp @Inject constructor(
    private val chuckerCreator: ChuckerCreator
) : DebugInterceptors {

    override operator fun invoke(): List<Interceptor> = listOf(
        chuckerCreator.create()
    )
}
