package au.com.alfie.ecomm.core.commons.extension

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IterableExtTest {

    @Test
    fun testFirstPredicateOrNullWithMatch() {
        val list = listOf("A", "B", "C", "D", "E")
        val result = list.firstPredicateOrNull {
            if (it == "D") "D" else null
        }
        assertTrue(result == "D")
    }

    @Test
    fun testFirstPredicateOrNullWithoutMatch() {
        val list = listOf("A", "B", "C", "D", "E")
        val result = list.firstPredicateOrNull {
            if (it == "F") "F" else null
        }
        assertNull(result)
    }

    @Test
    fun testFirstPredicateOrDefaultWithMatch() {
        val list = listOf("A", "B", "C", "D", "E")
        val result = list.firstPredicateOrDefault(
            { if (it == "D") "D" else null },
            { "H" }
        )
        assertEquals(result, "D")
    }

    @Test
    fun testFirstPredicateOrDefaultWithoutMatch() {
        val list = listOf("A", "B", "C", "D", "E")
        val result = list.firstPredicateOrDefault(
            { if (it == "F") "F" else null },
            { "H" }
        )
        assertEquals(result, "H")
    }
}
