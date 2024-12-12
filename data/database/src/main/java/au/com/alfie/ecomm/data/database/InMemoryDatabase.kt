package au.com.alfie.ecomm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import au.com.alfie.ecomm.data.database.navigation.NavigationEntryDao
import au.com.alfie.ecomm.data.database.navigation.model.NavigationEntryEntity

@Database(
    entities = [
        NavigationEntryEntity::class
    ],
    version = 1,
    exportSchema = true
)
internal abstract class InMemoryDatabase : RoomDatabase() {

    abstract fun navigationEntriesDao(): NavigationEntryDao
}
