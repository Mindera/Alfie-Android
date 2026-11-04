package com.mindera.alfie.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mindera.alfie.data.database.search.model.RecentSearchEntity
import com.mindera.alfie.data.database.wishlist.model.WishlistEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PersistentDatabaseMigrationsTest {

    private lateinit var database: PersistentDatabase

    @Before
    fun setupDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PersistentDatabase::class.java).build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun migration4To5_deletesOnlyBrandRecentSearches() = runTest {
        val recentSearchDao = database.recentSearchDao()
        val query = RecentSearchEntity(searchTerm = "shirt", searchTimestamp = 1L, type = RecentSearchEntity.Type.QUERY)
        recentSearchDao.saveRecentSearch(query)
        recentSearchDao.saveRecentSearch(
            RecentSearchEntity(
                searchTerm = "Alfie Basics",
                searchTimestamp = 2L,
                type = RecentSearchEntity.Type.BRAND,
                slug = "alfie-basics"
            )
        )
        database.wishlistDao().addToWishlist(WishlistEntity(id = "t-shirt"))

        MIGRATION_4_5.migrate(database.openHelper.writableDatabase)

        assertEquals(listOf(query), recentSearchDao.getRecentSearches().first())
        assertEquals(1, database.wishlistDao().getWishlistIds().first().size)
    }
}
