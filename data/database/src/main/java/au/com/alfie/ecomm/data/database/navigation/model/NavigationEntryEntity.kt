package au.com.alfie.ecomm.data.database.navigation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "navigation_entries")
data class NavigationEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("parent_id") val parentId: Int = 0,
    val title: String,
    val path: String,
    @ColumnInfo("nav_item_type") val navItemType: String,
    @Ignore val items: List<NavigationEntryEntity> = emptyList()
) {

    constructor(
        id: Int,
        parentId: Int,
        title: String,
        path: String,
        navItemType: String
    ) : this(id, parentId, title, path, navItemType, emptyList())
}
