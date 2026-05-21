package com.mindera.alfie.network.di

import javax.inject.Qualifier

/** Qualifier for the [com.apollographql.apollo.ApolloClient] targeting the legacy BFF (port 4000). */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LegacyClient

/** Qualifier for the [com.apollographql.apollo.ApolloClient] targeting the new BFF (port 3000). */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewClient
