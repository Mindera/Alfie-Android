package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.data.shared.attribute
import com.mindera.alfie.data.shared.attributesInfoData
import com.mindera.alfie.data.shared.hierarchy
import com.mindera.alfie.data.shared.hierarchyItemInfoData
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class CommonMapperTest {
    @Test
    fun testAttributesInfoMapper() = runTest {
        val expected = attribute
        val result = attributesInfoData.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun testHierarchyItemInfoMapper() = runTest {
        val expected = hierarchy
        val result = hierarchyItemInfoData.toDomain()

        assertEquals(expected, result)
    }
}
