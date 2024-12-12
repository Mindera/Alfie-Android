package au.com.alfie.ecomm.debug.operational.interceptor

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ChuckerCreator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun create() = ChuckerInterceptor(context)
}
