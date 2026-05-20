package com.mindera.alfie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindera.alfie.data.database.search.FeatureToggleDao
import com.mindera.alfie.data.database.search.model.FeatureToggleEntity

@Database(
    entities = [FeatureToggleEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class FeatureToggleDatabase : RoomDatabase() {

    abstract fun featureToggleDao(): FeatureToggleDao
}
