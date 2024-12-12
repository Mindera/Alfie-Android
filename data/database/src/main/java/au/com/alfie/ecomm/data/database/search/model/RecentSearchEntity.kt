package au.com.alfie.ecomm.data.database.search.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey
    @ColumnInfo("search_term")
    val searchTerm: String,
    @ColumnInfo("search_timestamp")
    val searchTimestamp: Long,
    val type: Type,
    val slug: String? = null
) {

    enum class Type {
        QUERY,
        BRAND
    }
}
