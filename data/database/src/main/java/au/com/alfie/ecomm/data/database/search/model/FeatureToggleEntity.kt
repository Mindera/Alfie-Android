package au.com.alfie.ecomm.data.database.search.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feature_toggle")
data class FeatureToggleEntity(
    @PrimaryKey
    @ColumnInfo("toggle_title")
    val toggleTitle: String,
    @ColumnInfo("enabled")
    val enabled: Boolean,
    @ColumnInfo("type")
    val type: String
)
