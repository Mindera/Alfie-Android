package com.mindera.alfie.data.database.navigation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "navigation_entries")
data class NavigationEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("parent_id") val parentId: Int? = null,
    val title: String,
    val path: String,
    @ColumnInfo("nav_item_type") val navItemType: String,
    /**
     * Whether this entry drills into a sub-category list. Persisted rather than derived, because the
     * read path re-queries children by [parentId] and never rehydrates [items] — so this column is
     * the only place the tree shape survives. Single source of truth for the category row's chevron
     * and for the tap-handling branch, so the two cannot disagree.
     */
    @ColumnInfo("has_children") val hasChildren: Boolean = false,
    @Ignore val items: List<NavigationEntryEntity> = emptyList()
) {

    constructor(
        id: Int,
        parentId: Int,
        title: String,
        path: String,
        navItemType: String,
        hasChildren: Boolean
    ) : this(id, parentId, title, path, navItemType, hasChildren, emptyList())
}
