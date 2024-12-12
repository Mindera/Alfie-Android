package au.com.alfie.ecomm.data.database.search

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import au.com.alfie.ecomm.data.database.search.model.FeatureToggleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeatureToggleDao {

    @Query("SELECT * FROM feature_toggle ORDER BY toggle_title ASC")
    fun getAllFeatureTogglesAsFlow(): Flow<List<FeatureToggleEntity>>

    @Query("SELECT * FROM feature_toggle WHERE toggle_title = :toggleTitle")
    fun getFeatureTogglesByNameAsFlow(toggleTitle: String): Flow<List<FeatureToggleEntity>>

    @Transaction
    suspend fun insertFeatureToggle(entries: List<FeatureToggleEntity>) {
        val list = getAllFeatureToggles().toMutableList()
        entries.forEach {
            val isExist = getFeatureToggleByTitle(it.toggleTitle)
            if (isExist.isEmpty()) {
                saveFeatureToggle(it)
            } else {
                if (list.isNotEmpty() && list.contains(isExist[0])) {
                    list.remove(isExist[0])
                }
            }
        }
        list.forEach {
            deleteFeatureToggle(it.toggleTitle)
        }
    }

    @Upsert
    suspend fun saveFeatureToggle(search: FeatureToggleEntity)

    @Query("SELECT * FROM feature_toggle WHERE toggle_title = :toggleTitle")
    suspend fun getFeatureToggleByTitle(toggleTitle: String): List<FeatureToggleEntity>

    @Query("SELECT * FROM feature_toggle ORDER BY toggle_title ASC")
    suspend fun getAllFeatureToggles(): List<FeatureToggleEntity>

    @Query("UPDATE feature_toggle SET enabled = :enabled WHERE toggle_title = :toggleTitle")
    suspend fun updateFeatureToggle(toggleTitle: String, enabled: Boolean)

    @Query("DELETE FROM feature_toggle WHERE toggle_title = :toggleTitle")
    suspend fun deleteFeatureToggle(toggleTitle: String)
}
