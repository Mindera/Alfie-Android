package com.mindera.alfie.data.navigation.remote.mapper

import com.mindera.alfie.data.navigation.navEntries
import com.mindera.alfie.data.navigation.navEntriesData
import com.mindera.alfie.data.navigation.navEntryEntitiesFromGraph
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
