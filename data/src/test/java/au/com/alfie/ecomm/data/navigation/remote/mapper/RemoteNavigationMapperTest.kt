package au.com.alfie.ecomm.data.navigation.remote.mapper

import au.com.alfie.ecomm.data.navigation.navEntries
import au.com.alfie.ecomm.data.navigation.navEntriesData
import au.com.alfie.ecomm.data.navigation.navEntryEntitiesFromGraph
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class RemoteNavigationMapperTest {

    @Test
    fun testRemoteNavigationDomainMapper() = runTest {
        val expected = navEntries
        val result = navEntriesData.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun testRemoteNavigationEntityMapper() = runTest {
        val expected = navEntryEntitiesFromGraph
        val result = navEntriesData.toEntity()

        assertEquals(expected, result)
    }
}
