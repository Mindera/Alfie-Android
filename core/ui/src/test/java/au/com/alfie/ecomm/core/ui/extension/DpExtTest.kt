package au.com.alfie.ecomm.core.ui.extension

import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DpExtTest {

    @Test
    fun `400 dp width should be considered Compact window type`() {
        val result = 400.dp.widthWindowType()

        assertEquals(WindowType.Compact, result)
    }

    @Test
    fun `700 dp width should be considered Medium window type`() {
        val result = 700.dp.widthWindowType()

        assertEquals(WindowType.Medium, result)
    }

    @Test
    fun `900 dp width should be considered Expanded window type`() {
        val result = 900.dp.widthWindowType()

        assertEquals(WindowType.Expanded, result)
    }

    @Test
    fun `300 dp height should be considered Compact window type`() {
        val result = 300.dp.heightWindowType()

        assertEquals(WindowType.Compact, result)
    }

    @Test
    fun `500 dp height should be considered Medium window type`() {
        val result = 500.dp.heightWindowType()

        assertEquals(WindowType.Medium, result)
    }

    @Test
    fun `1000 dp height should be considered Expanded window type`() {
        val result = 1000.dp.heightWindowType()

        assertEquals(WindowType.Expanded, result)
    }
}
