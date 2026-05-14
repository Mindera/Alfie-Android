package com.mindera.alfie.data.navigation.cache.mapper

import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity
import com.mindera.alfie.repository.navigation.model.NavEntry
import com.mindera.alfie.repository.navigation.model.NavItemType

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
