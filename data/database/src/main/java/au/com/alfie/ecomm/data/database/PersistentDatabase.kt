package au.com.alfie.ecomm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import au.com.alfie.ecomm.data.database.search.RecentSearchDao
import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity

@Database(
    entities = [
        RecentSearchEntity::class
    ],
    version = 2,
    exportSchema = true
)
internal abstract class PersistentDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao
}
