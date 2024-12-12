package au.com.alfie.ecomm.feature.pdp

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.core.navigation.arguments.productDetailsNavArgs
import au.com.alfie.ecomm.feature.pdp.destinations.ProductDetailsScreenDestination
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
