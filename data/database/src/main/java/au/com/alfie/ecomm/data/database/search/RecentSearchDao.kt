package au.com.alfie.ecomm.data.database.search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query("SELECT * FROM recent_searches ORDER BY search_timestamp DESC")
    fun getRecentSearches(): Flow<List<RecentSearchEntity>>

    @Upsert
    suspend fun saveRecentSearch(search: RecentSearchEntity)

    @Delete
    suspend fun deleteRecentSearch(search: RecentSearchEntity)

    @Query("DELETE FROM recent_searches WHERE search_term NOT IN (SELECT search_term FROM recent_searches ORDER BY search_timestamp DESC LIMIT 5)")
    suspend fun clearOldSearches()

    @Query("DELETE FROM recent_searches")
    suspend fun clearRecentSearches()
}
