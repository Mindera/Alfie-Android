package com.mindera.alfie.feature.pdp

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.core.deeplink.DeeplinkInterpreter
import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.core.deeplink.model.DeeplinkInstance
import com.mindera.alfie.core.deeplink.model.DeeplinkSpec
import com.mindera.alfie.core.deeplink.model.deeplinkSpec
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.feature.pdp.destinations.ProductDetailsScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductDetailsDeeplinks @Inject constructor() : DeeplinkGroup {

    companion object {
        private const val PRODUCT_FIXED_SEGMENT = "product"
        private const val PRODUCT_ARGUMENT_SEGMENT = "handle"
        private const val VARIANT_QUERY_PARAMETER = "variantId"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/product/{handle}[?variantId={id}]
                deeplinkSpec {
                    appendFixedPathSegment(PRODUCT_FIXED_SEGMENT)
                    appendArgumentPathSegment(PRODUCT_ARGUMENT_SEGMENT)
                    appendQueryParameter(VARIANT_QUERY_PARAMETER)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val handle: String = instance.pathArguments[PRODUCT_ARGUMENT_SEGMENT].orEmpty()
                return DeeplinkResult.NavigateTo(
                    direction = ProductDetailsScreenDestination(
                        productDetailsNavArgs(
                            handle = handle,
                            variantId = instance.queryArguments[VARIANT_QUERY_PARAMETER]
                        )
                    )
                )
            }
        }
    )
}
