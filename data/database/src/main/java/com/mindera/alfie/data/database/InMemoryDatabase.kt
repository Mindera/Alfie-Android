package com.mindera.alfie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindera.alfie.data.database.navigation.NavigationEntryDao
import com.mindera.alfie.data.database.navigation.model.NavigationEntryEntity

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
