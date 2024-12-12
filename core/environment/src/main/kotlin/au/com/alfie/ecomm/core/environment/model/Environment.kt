package au.com.alfie.ecomm.core.environment.model

sealed class Environment(
    open val graphQLUrl: String,
    open val webUrl: String
) {

    data class Dev(
        override val graphQLUrl: String,
        override val webUrl: String
    ) : Environment(
        graphQLUrl = graphQLUrl,
        webUrl = webUrl
    )

    data class PreProd(
        override val graphQLUrl: String,
        override val webUrl: String
    ) : Environment(
        graphQLUrl = graphQLUrl,
        webUrl = webUrl
    )

    data class Prod(
        override val graphQLUrl: String,
        override val webUrl: String
    ) : Environment(
        graphQLUrl = graphQLUrl,
        webUrl = webUrl
    )

    data class Custom(
        override val graphQLUrl: String,
        override val webUrl: String
    ) : Environment(
        graphQLUrl = graphQLUrl,
        webUrl = webUrl
    )
}
