package com.mindera.alfie.network.interceptor

import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation.Data
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.mindera.alfie.network.exception.ExceptionErrorCodes.INTERNAL_HTTP_ERROR
import com.mindera.alfie.network.exception.GraphNetworkException.NetworkException
import com.mindera.alfie.network.util.ConnectionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class NetworkStatusInterceptor @Inject constructor(
    private val connectionManager: ConnectionManager
) : ApolloInterceptor {

    override fun <D : Data> intercept(
        request: ApolloRequest<D>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<D>> {
        return chain.proceed(request).onEach {
            if (connectionManager.isConnected.not()) {
                throw NetworkException(
                    code = INTERNAL_HTTP_ERROR.code,
                    message = "No internet connection"
                )
            }
        }
    }
}
