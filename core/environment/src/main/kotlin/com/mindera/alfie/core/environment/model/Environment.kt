package com.mindera.alfie.core.environment.model

sealed class Environment(
    open val graphQLUrl: String,
    open val legacyGraphQLUrl: String,
    open val webUrl: String
) {

    data class Dev(
        override val graphQLUrl: String,
        override val webUrl: String,
        // Mark as same for now to support unit-test, we'll get rid of this after migration anw
        override val legacyGraphQLUrl: String = graphQLUrl
    ) : Environment(
        graphQLUrl = graphQLUrl,
        legacyGraphQLUrl = legacyGraphQLUrl,
        webUrl = webUrl
    )

    data class PreProd(
        override val graphQLUrl: String,
        override val webUrl: String,
        // Mark as same for now to support unit-test, we'll get rid of this after migration anw
        override val legacyGraphQLUrl: String = graphQLUrl
    ) : Environment(
        graphQLUrl = graphQLUrl,
        legacyGraphQLUrl = legacyGraphQLUrl,
        webUrl = webUrl
    )

    data class Prod(
        override val graphQLUrl: String,
        override val webUrl: String,
        // Mark as same for now to support unit-test, we'll get rid of this after migration anw
        override val legacyGraphQLUrl: String =  graphQLUrl
    ) : Environment(
        graphQLUrl = graphQLUrl,
        legacyGraphQLUrl = legacyGraphQLUrl,
        webUrl = webUrl
    )

    data class Custom(
        override val graphQLUrl: String,
        override val webUrl: String,
        // Mark as same for now to support unit-test, we'll get rid of this after migration anw
        override val legacyGraphQLUrl: String = graphQLUrl
    ) : Environment(
        graphQLUrl = graphQLUrl,
        legacyGraphQLUrl = legacyGraphQLUrl,
        webUrl = webUrl
    )
}
