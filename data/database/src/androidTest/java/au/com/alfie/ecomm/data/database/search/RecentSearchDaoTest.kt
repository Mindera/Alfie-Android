package au.com.alfie.ecomm.data.database.search

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.data.database.PersistentDatabase
import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineExtension::class)
class RecentSearchDaoTest {

    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var database: PersistentDatabase

    @Before
    fun setupDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            PersistentDatabase::class.java
        ).build()
        recentSearchDao = database.recentSearchDao()
    }

    @After
    fun closeDatabase() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun testSaveRecentSearch() = runTest {
        recentSearchDao.getRecentSearches().distinctUntilChanged().test {
            assertEquals(0, awaitItem().size) // Empty initially

            val entity = generateRecentSearch("Lorem")
            recentSearchDao.saveRecentSearch(entity)
            val result = awaitItem()

            assertEquals(1, result.size)
            assertEquals(entity, result.first())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testSaveMultipleRecentSearchShouldUpdateFlow() = runTest {
        val entity = generateRecentSearch("Ipsum")

        recentSearchDao.getRecentSearches().distinctUntilChanged().test {
            assertEquals(0, awaitItem().size) // Empty initially

            recentSearchDao.saveRecentSearch(entity)
            val first = awaitItem()
            assertEquals(1, first.size)
            assertEquals(entity, first.first())

            recentSearchDao.saveRecentSearch(entity.copy(searchTerm = "abc"))
            val second = awaitItem()
            assertEquals(2, second.size)
            assertEquals("abc", second.last().searchTerm)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testSaveRecentSearchWithSameTermReplaceExisting() = runTest {
        recentSearchDao.getRecentSearches().distinctUntilChanged().test {
            assertEquals(0, awaitItem().size) // Empty initially

            val firstEntity = generateRecentSearch("Lorem")
            recentSearchDao.saveRecentSearch(firstEntity)
            val first = awaitItem()
            assertEquals(1, first.size)
            assertEquals(firstEntity, first.first())

            val secondEntity = generateRecentSearch("Lorem")
            recentSearchDao.saveRecentSearch(secondEntity)
            val second = awaitItem()
            assertEquals(1, second.size)
            assertEquals(secondEntity, second.first())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDeleteRecentSearch() = runTest {
        recentSearchDao.getRecentSearches().distinctUntilChanged().test {
            assertEquals(0, awaitItem().size) // Empty initially

            val entity = generateRecentSearch("Lorem")
            recentSearchDao.saveRecentSearch(entity)
            val first = awaitItem()
            assertEquals(1, first.size)
            assertEquals(entity, first.first())

            recentSearchDao.deleteRecentSearch(entity)
            val second = awaitItem()
            assertEquals(0, second.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testClearOldSearches() = runTest {
        val entities = (1..12).map {
            generateRecentSearch(
                searchTerm = "Lorem$it",
                searchTimestamp = it.toLong()
            )
        }

        recentSearchDao.getRecentSearches().distinctUntilChanged().test {
            assertEquals(0, awaitItem().size) // Empty initially

            entities.forEachIndexed { i, entity ->
                recentSearchDao.saveRecentSearch(entity)
                val items = awaitItem()
                assertEquals(i + 1, items.size)
                assertEquals(entity, items.first())
            }

            recentSearchDao.clearOldSearches()
            val result = awaitItem()
            assertEquals(5, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun generateRecentSearch(
        searchTerm: String,
        searchTimestamp: Long = System.currentTimeMillis()
    ) = RecentSearchEntity(
        searchTerm = searchTerm,
        searchTimestamp = searchTimestamp,
        type = RecentSearchEntity.Type.QUERY,
        slug = null
    )
}
