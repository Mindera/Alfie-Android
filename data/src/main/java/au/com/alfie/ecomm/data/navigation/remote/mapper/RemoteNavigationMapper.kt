package au.com.alfie.ecomm.data.navigation.remote.mapper

import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity
import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery
import au.com.alfie.ecomm.graphql.fragment.NavMenuItemContainer
import au.com.alfie.ecomm.graphql.fragment.NavMenuItemInfo
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.navigation.model.NavItemType

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
    navItemType = NavItemType.UNKNOWN.toString(),
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
