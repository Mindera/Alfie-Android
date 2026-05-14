package com.mindera.alfie.data.navigation.cache.mapper

import com.mindera.alfie.data.navigation.mappedNavEntries
import com.mindera.alfie.data.navigation.mappedNavEntryEntities
import com.mindera.alfie.data.navigation.navEntryEntities
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class CacheNavigationMapperTest {

    @Test
    fun testCacheNavigationDomainMapper() = runTest {
        val expected = mappedNavEntries
        val result = navEntryEntities.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun testCacheNavigationEntityMapper() = runTest {
        val expected = mappedNavEntryEntities
        val result = mappedNavEntries.toEntity()

        assertEquals(expected, result)
    }
}
