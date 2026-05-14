package com.mindera.alfie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindera.alfie.data.database.search.RecentSearchDao
import com.mindera.alfie.data.database.search.model.RecentSearchEntity

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
