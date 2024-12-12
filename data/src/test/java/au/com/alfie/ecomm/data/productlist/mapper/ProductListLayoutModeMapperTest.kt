package au.com.alfie.ecomm.data.productlist.mapper

import au.com.alfie.ecomm.data.datastore.UserPreferencesProto.ProductListLayoutModeProto
import au.com.alfie.ecomm.repository.productlist.model.ProductListLayoutMode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductListLayoutModeMapperTest {

    @Test
    fun `toDomain - when Proto GRID then map to ProductListLayoutMode GRID`() {
        val expected = ProductListLayoutMode.GRID
        val result = ProductListLayoutModeProto.GRID.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun `toDomain - when Proto COLUMN then map to ProductListLayoutMode COLUMN`() {
        val expected = ProductListLayoutMode.COLUMN
        val result = ProductListLayoutModeProto.COLUMN.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun `toDomain - when Proto UNRECOGNIZED then map to ProductListLayoutMode GRID`() {
        val expected = ProductListLayoutMode.GRID
        val result = ProductListLayoutModeProto.UNRECOGNIZED.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun `toProto - when ProductListLayoutMode GRID then map to Proto GRID`() {
        val expected = ProductListLayoutModeProto.GRID
        val result = ProductListLayoutMode.GRID.toProto()

        assertEquals(expected, result)
    }

    @Test
    fun `toProto - when ProductListLayoutMode COLUMN then map to Proto COLUMN`() {
        val expected = ProductListLayoutModeProto.COLUMN
        val result = ProductListLayoutMode.COLUMN.toProto()

        assertEquals(expected, result)
    }
}
