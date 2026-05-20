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
        private const val PRODUCT_ARGUMENT_SEGMENT = "id"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/product/{id}
                deeplinkSpec {
                    appendFixedPathSegment(PRODUCT_FIXED_SEGMENT)
                    appendArgumentPathSegment(PRODUCT_ARGUMENT_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val productId: String = instance.pathArguments[PRODUCT_ARGUMENT_SEGMENT]
                    ?.split("-")?.lastOrNull().orEmpty()
                return DeeplinkResult.NavigateTo(
                    direction = ProductDetailsScreenDestination(
                        productDetailsNavArgs(id = productId)
                    )
                )
            }
        }
    )
}
