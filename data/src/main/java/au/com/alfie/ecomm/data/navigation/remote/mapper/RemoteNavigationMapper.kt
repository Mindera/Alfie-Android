package au.com.alfie.ecomm.data.navigation.remote.mapper

import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity
import au.com.alfie.ecomm.graphql.NavEntriesByHandleQuery
import au.com.alfie.ecomm.graphql.fragment.NavMenuItemContainer
import au.com.alfie.ecomm.graphql.fragment.NavMenuItemInfo
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.navigation.model.NavItemType

internal fun NavEntriesByHandleQuery.Data.toDomain(): List<NavEntry> =
    navigation?.items?.map { it.navMenuItemContainer.toDomain() }.orEmpty()

private fun NavMenuItemContainer.toDomain() = NavEntry(
    id = 0,
    title = navMenuItemInfo.title,
    type = NavItemType.from(navMenuItemInfo.type.rawValue),
    url = navMenuItemInfo.url,
    items = items?.map { it.navMenuItemInfo.toDomain() }.orEmpty()
)

private fun NavMenuItemInfo.toDomain() = NavEntry(
    id = 0,
    title = title,
    type = NavItemType.from(this.type.rawValue),
    url = url
)

internal fun NavEntriesByHandleQuery.Data.toEntity(): List<NavigationEntryEntity> =
    navigation?.items?.map { it.navMenuItemContainer.toEntity() }.orEmpty()

private fun NavMenuItemContainer.toEntity(): NavigationEntryEntity = NavigationEntryEntity(
    title = navMenuItemInfo.title,
    path = navMenuItemInfo.url.orEmpty(),
    navItemType = navMenuItemInfo.type.rawValue,
    items = items?.map { it.navMenuItemInfo.toEntity() }.orEmpty()
)

private fun NavMenuItemInfo.toEntity() = NavigationEntryEntity(
    id = 0,
    path = url.orEmpty(),
    title = title,
    navItemType = type.rawValue
)
