package au.com.alfie.ecomm.network.interceptor

import au.com.alfie.ecomm.network.exception.ExceptionErrorCodes.INTERNAL_HTTP_ERROR
import au.com.alfie.ecomm.network.exception.GraphNetworkException.NetworkException
import au.com.alfie.ecomm.network.util.ConnectionManager
import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation.Data
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
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
