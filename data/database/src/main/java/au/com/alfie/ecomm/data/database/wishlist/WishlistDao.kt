package au.com.alfie.ecomm.data.database.wishlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import au.com.alfie.ecomm.data.database.wishlist.model.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(product: WishlistEntity)

    @Query("DELETE FROM wishlist WHERE id = :productId")
    suspend fun removeFromWishlist(productId: String)

    @Query("SELECT * FROM wishlist")
    fun getWishlistIds(): Flow<List<WishlistEntity>>
}