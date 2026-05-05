package au.com.alfie.ecomm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import au.com.alfie.ecomm.data.database.search.RecentSearchDao
import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity
import au.com.alfie.ecomm.data.database.wishlist.WishlistDao
import au.com.alfie.ecomm.data.database.wishlist.model.WishlistEntity

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
