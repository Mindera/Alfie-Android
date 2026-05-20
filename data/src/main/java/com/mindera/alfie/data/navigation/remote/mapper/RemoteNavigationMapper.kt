package com.mindera.alfie.data.navigation.remote.mapper

import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity
import com.mindera.alfie.graphql.NavEntriesByHandleQuery
import com.mindera.alfie.graphql.fragment.NavMenuItemContainer
import com.mindera.alfie.graphql.fragment.NavMenuItemInfo
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.navigation.model.NavItemType

internal fun NavEntriesByHandleQuery.Data.toDomain(): List<NavEntry> =
    navigation.map { it.toDomain() }

private fun NavEntriesByHandleQuery.Navigation.toDomain() = NavEntry(
    id = 0,
    title = title,
    type = NavItemType.from(type.rawValue),
    url = url
)

internal fun NavEntriesByHandleQuery.Data.toEntity(): List<NavigationEntryEntity> =
    navigation.map { it.toEntity() }

private fun NavEntriesByHandleQuery.Navigation.toEntity(): NavigationEntryEntity = NavigationEntryEntity(
    title = title,
    path = url.orEmpty(),
    navItemType = type.rawValue,
    items = items?.map { it.navMenuItemContainer.toEntity() } ?: emptyList()
)

private fun NavMenuItemContainer.toEntity(): NavigationEntryEntity = NavigationEntryEntity(
    title = navMenuItemInfo.title,
    path = navMenuItemInfo.url.orEmpty(),
    navItemType = navMenuItemInfo.type.rawValue,
    items = items?.map { it.navMenuItemInfo.toEntity() }.orEmpty()
)

private fun NavMenuItemInfo.toEntity() = NavigationEntryEntity(
    path = url.orEmpty(),
    title = title,
    navItemType = type.rawValue
)
