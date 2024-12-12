package au.com.alfie.ecomm.data.datastore.user

import androidx.datastore.core.DataStore
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.core.test.TestDispatcherProvider
import au.com.alfie.ecomm.data.datastore.UserPreferencesProto
import au.com.alfie.ecomm.data.datastore.UserPreferencesProto.ProductListLayoutModeProto.COLUMN
import au.com.alfie.ecomm.data.datastore.UserPreferencesProto.ProductListLayoutModeProto.GRID
import au.com.alfie.ecomm.data.datastore.userPreferencesProto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class UserPreferencesDataSourceImplTest {

    @RelaxedMockK
    private lateinit var dataStore: DataStore<UserPreferencesProto>

    private lateinit var subject: UserPreferencesDataSourceImpl

    @BeforeEach
    fun setUp() {
        subject = UserPreferencesDataSourceImpl(
            dataStore = dataStore,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @Test
    fun `default layout mode is GRID`() = runTest {
        val preferences = userPreferencesProto { }

        assertEquals(GRID, preferences.productListLayoutMode)
    }

    @Test
    fun `updateProductListLayoutMode - when layout mode is GRID then call update data from data store`() = runTest {
        subject.updateProductListLayoutMode(GRID)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `updateProductListLayoutMode - when layout mode is COLUMN then call update data from data store`() = runTest {
        subject.updateProductListLayoutMode(COLUMN)

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `getProductListLayoutMode - then get data from the data store`() = runTest {
        val preferences = userPreferencesProto {
            productListLayoutMode = COLUMN
        }
        coEvery { dataStore.data } returns flowOf(preferences)

        val result = subject.getProductListLayoutMode()

        assertEquals(COLUMN, result)
    }
}
