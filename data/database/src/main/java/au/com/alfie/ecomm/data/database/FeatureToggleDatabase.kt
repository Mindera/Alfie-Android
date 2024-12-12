package au.com.alfie.ecomm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import au.com.alfie.ecomm.data.database.search.FeatureToggleDao
import au.com.alfie.ecomm.data.database.search.model.FeatureToggleEntity

@Database(
    entities = [FeatureToggleEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class FeatureToggleDatabase : RoomDatabase() {

    abstract fun featureToggleDao(): FeatureToggleDao
}
