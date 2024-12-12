package au.com.alfie.ecomm.data.navigation.cache.mapper

import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity
import au.com.alfie.ecomm.repository.navigation.model.NavEntry
import au.com.alfie.ecomm.repository.navigation.model.NavItemType

internal fun List<NavEntry>.toEntity(): List<NavigationEntryEntity> = map { it.toEntity() }

private fun NavEntry.toEntity(): NavigationEntryEntity = NavigationEntryEntity(
    title = title,
    path = url.orEmpty(),
    navItemType = type.name,
    items = items.toEntity()
)

internal fun List<NavigationEntryEntity>.toDomain(): List<NavEntry> = map { it.toDomain() }

internal fun NavigationEntryEntity.toDomain(): NavEntry = NavEntry(
    id = id,
    title = title,
    type = NavItemType.from(navItemType),
    url = path
)
