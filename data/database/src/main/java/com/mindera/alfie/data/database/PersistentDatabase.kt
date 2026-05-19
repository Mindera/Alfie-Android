package com.mindera.alfie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindera.alfie.data.database.search.RecentSearchDao
import com.mindera.alfie.data.database.search.model.RecentSearchEntity
import com.mindera.alfie.data.database.wishlist.WishlistDao
import com.mindera.alfie.data.database.wishlist.model.WishlistEntity

@Database(
    entities = [
        RecentSearchEntity::class,
        WishlistEntity::class
    ],
    version = 3,
    exportSchema = true
)
internal abstract class PersistentDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao

    abstract fun wishlistDao(): WishlistDao
}
