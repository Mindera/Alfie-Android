package au.com.alfie.ecomm.core.commons.extension

import au.com.alfie.ecomm.core.commons.util.TestModel
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringExtTest {

    @Test
    fun testStringCapitalize() {
        val text = "gucci gucci"
        val expected = "Gucci gucci"
        val capitalized = text.capitalize()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringAlreadyCapitalize() {
        val text = "Gucci gucci"
        val expected = "Gucci gucci"
        val capitalized = text.capitalize()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringCapitalizeAllWorlds() {
        val text = "gucci gucci"
        val expected = "Gucci Gucci"
        val capitalized = text.capitalizeAllWorlds()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringAlreadyCapitalizeAllWorlds() {
        val text = "Gucci Gucci"
        val expected = "Gucci Gucci"
        val capitalized = text.capitalizeAllWorlds()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringUppercaseCapitalizeAllWorlds() {
        val text = "GUCCI GUCCI"
        val expected = "Gucci Gucci"
        val capitalized = text.capitalizeAllWorlds()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringToIntOrZeroInvalid() {
        val text = "1a"
        val expected = 0
        val capitalized = text.toIntOrZero()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringToIntOrZeroEmpty() {
        val text = ""
        val expected = 0
        val capitalized = text.toIntOrZero()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testStringToIntOrZeroValid() {
        val text = "312"
        val expected = 312
        val capitalized = text.toIntOrZero()
        assertEquals(capitalized, expected)
    }

    @Test
    fun testIsNotNullOrBlankInvalid() {
        val text = " "
        assertFalse(text.isNotNullOrBlank())
    }

    @Test
    fun testIsNotNullOrBlankValid() {
        val text = "1234 abc"
        assertTrue(text.isNotNullOrBlank())
    }

    @Test
    fun testStringFromJson() {
        val json = "{ \"isEnabled\": true, \"label\": \"something\", \"position\": 1, \"attributes\": [\"something\"] }"
        val expected = TestModel(
            isEnabled = true,
            label = "something",
            position = 1,
            attributes = listOf("something")
        )
        val result = json.fromJson<TestModel>()

        assertEquals(expected, result)
    }

    @Test
    fun testRemoveAccents() {
        val test = "Ábcdeêö"
        val expected = "Abcdeeo"
        val result = test.removeAccents()

        assertEquals(expected, result)
    }
}
