package au.com.alfie.ecomm.feature.plp

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.core.deeplink.DeeplinkInterpreter
import au.com.alfie.ecomm.core.deeplink.DeeplinkResult
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import au.com.alfie.ecomm.core.navigation.arguments.productlist.ProductListType
import au.com.alfie.ecomm.core.navigation.arguments.productlist.productListNavArgs
import au.com.alfie.ecomm.feature.plp.destinations.ProductListScreenDestination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListDeeplinks @Inject constructor() : DeeplinkGroup {

    companion object {
        private const val BRAND_FIXED_SEGMENT = "brand"
        private const val BRANDS_FIXED_SEGMENT = "brands"
        private const val BRAND_ARGUMENT_SEGMENT = "brand"
        private const val CATEGORY_ARGUMENT_SEGMENT = "category"
        private const val SUB_CATEGORY1_ARGUMENT_SEGMENT = "sub-category1"
        private const val SUB_CATEGORY2_ARGUMENT_SEGMENT = "sub-category2"
        private const val SUB_CATEGORY3_ARGUMENT_SEGMENT = "sub-category3"
        private const val SUB_CATEGORY4_ARGUMENT_SEGMENT = "sub-category4"
        private const val SEARCH_FIXED_SEGMENT = "search"
        private const val SEARCH_QUERY_PARAMETER = "q"
    }

    override val interpreters: List<DeeplinkInterpreter> = listOf(
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/brand/{brand}
                deeplinkSpec {
                    appendFixedPathSegment(BRAND_FIXED_SEGMENT)
                    appendArgumentPathSegment(BRAND_ARGUMENT_SEGMENT)
                },
                // https://www.alfie.com/brand/{brand}/{category}
                deeplinkSpec {
                    appendFixedPathSegment(BRAND_FIXED_SEGMENT)
                    appendArgumentPathSegment(BRAND_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(CATEGORY_ARGUMENT_SEGMENT)
                },
                // https://www.alfie.com/brands/{brand}
                deeplinkSpec {
                    appendFixedPathSegment(BRANDS_FIXED_SEGMENT)
                    appendArgumentPathSegment(BRAND_ARGUMENT_SEGMENT)
                },
                // https://www.alfie.com/brands/{brand}/{category}
                deeplinkSpec {
                    appendFixedPathSegment(BRANDS_FIXED_SEGMENT)
                    appendArgumentPathSegment(BRAND_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(CATEGORY_ARGUMENT_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val brand: String = instance.pathArguments[BRAND_ARGUMENT_SEGMENT].orEmpty()
                val type = ProductListType.Brand.Slug(brand)

                return DeeplinkResult.NavigateTo(
                    direction = ProductListScreenDestination(
                        productListNavArgs(type = type)
                    )
                )
            }
        },
        object : DeeplinkInterpreter {
            val regex = Regex(
                "(designer|women|men|shoes|bags-and-accessories|beauty|kids|home-and-food|electrical|sale|kids-and-baby|toys|food-and-wine|home-and-electrical)"
            )
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/{category}
                deeplinkSpec {
                    appendArgumentPathSegment(
                        argumentName = CATEGORY_ARGUMENT_SEGMENT,
                        pattern = regex
                    )
                },
                // https://www.alfie.com/{category}/{sub-category1}
                deeplinkSpec {
                    appendArgumentPathSegment(
                        argumentName = CATEGORY_ARGUMENT_SEGMENT,
                        pattern = regex
                    )
                    appendArgumentPathSegment(SUB_CATEGORY1_ARGUMENT_SEGMENT)
                },
                // https://www.alfie.com/{category}/{sub-category1}/{sub-category2}
                deeplinkSpec {
                    appendArgumentPathSegment(
                        argumentName = CATEGORY_ARGUMENT_SEGMENT,
                        pattern = regex
                    )
                    appendArgumentPathSegment(SUB_CATEGORY1_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(SUB_CATEGORY2_ARGUMENT_SEGMENT)
                },
                // https://www.alfie.com/{category}/{sub-category1}/{sub-category2}/{sub-category3}
                deeplinkSpec {
                    appendArgumentPathSegment(
                        argumentName = CATEGORY_ARGUMENT_SEGMENT,
                        pattern = regex
                    )
                    appendArgumentPathSegment(SUB_CATEGORY1_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(SUB_CATEGORY2_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(SUB_CATEGORY3_ARGUMENT_SEGMENT)
                },
                // https://www.alfie.com/{category}/{sub-category1}/{sub-category2}/{sub-category3}/{sub-category4}
                deeplinkSpec {
                    appendArgumentPathSegment(
                        argumentName = CATEGORY_ARGUMENT_SEGMENT,
                        pattern = regex
                    )
                    appendArgumentPathSegment(SUB_CATEGORY1_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(SUB_CATEGORY2_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(SUB_CATEGORY3_ARGUMENT_SEGMENT)
                    appendArgumentPathSegment(SUB_CATEGORY4_ARGUMENT_SEGMENT)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val category: String = instance.pathArguments[CATEGORY_ARGUMENT_SEGMENT].orEmpty()
                val subCategory1: String? = instance.pathArguments[SUB_CATEGORY1_ARGUMENT_SEGMENT]
                val subCategory2: String? = instance.pathArguments[SUB_CATEGORY2_ARGUMENT_SEGMENT]
                val subCategory3: String? = instance.pathArguments[SUB_CATEGORY3_ARGUMENT_SEGMENT]
                val subCategory4: String? = instance.pathArguments[SUB_CATEGORY4_ARGUMENT_SEGMENT]
                val type = ProductListType.Category.Slug(subCategory4 ?: subCategory3 ?: subCategory2 ?: subCategory1 ?: category)

                return DeeplinkResult.NavigateTo(
                    direction = ProductListScreenDestination(
                        productListNavArgs(type = type)
                    )
                )
            }
        },
        object : DeeplinkInterpreter {
            override val specs: List<DeeplinkSpec> = listOf(
                // https://www.alfie.com/search?q={q}
                deeplinkSpec {
                    appendFixedPathSegment(SEARCH_FIXED_SEGMENT)
                    appendQueryParameter(SEARCH_QUERY_PARAMETER)
                }
            )

            override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult {
                val query = instance.queryArguments[SEARCH_QUERY_PARAMETER]
                val type = ProductListType.Search(query.orEmpty())

                return DeeplinkResult.NavigateTo(
                    direction = ProductListScreenDestination(
                        productListNavArgs(type = type)
                    )
                )
            }
        }
    )
}
