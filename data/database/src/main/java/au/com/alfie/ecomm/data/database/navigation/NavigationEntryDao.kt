package au.com.alfie.ecomm.data.database.navigation

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity

@Dao
interface NavigationEntryDao {

    @Transaction
    suspend fun insert(entries: List<NavigationEntryEntity>): List<NavigationEntryEntity> {
        return buildList {
            entries.forEach { entry ->
                val entryId = insertEntry(entry).toInt()
                add(element = entry.copy(id = entryId))
                val subItems = entry.items.map {
                    it.copy(parentId = entryId)
                }
                addAll(elements = insert(subItems))
            }
        }
    }

    @Upsert
    suspend fun insertEntry(entities: NavigationEntryEntity): Long

    @Query("SELECT * FROM navigation_entries WHERE :parentId = parent_id")
    suspend fun getByParentId(parentId: Int): List<NavigationEntryEntity>
}
