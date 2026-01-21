package au.com.alfie.ecomm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import au.com.alfie.ecomm.data.database.wishlist.WishlistDao
import au.com.alfie.ecomm.data.database.wishlist.model.WishlistEntity

@Database(
    entities = [WishlistEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class WishlistDatabase : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao
}
