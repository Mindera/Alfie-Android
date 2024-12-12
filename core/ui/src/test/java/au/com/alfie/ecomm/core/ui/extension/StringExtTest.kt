package au.com.alfie.ecomm.core.ui.extension

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringExtTest {

    @Test
    fun `The term 'Lor' must be correctly highlighted in 'Lorem Ipsum'`() {
        val full = "Lorem Ipsum"
        val highlight = "Lor"
        val result = full.highlightTerm(highlight)

        assertEquals(1, result.spanStyles.size)
        result.spanStyles.forEach {
            assertEquals("Lor", full.substring(it.start, it.end))
        }
    }

    @Test
    fun `There should be no highlights when highlighting 'abc' in 'Lorem Ipsum'`() {
        val full = "Lorem Ipsum"
        val highlight = "abc"
        val result = full.highlightTerm(highlight)

        assertEquals(0, result.spanStyles.size)
    }

    @Test
    fun `The term 'Lor' must be highlighted twice in 'Lorem Lorem'`() {
        val full = "Lorem Lorem"
        val highlight = "Lor"
        val result = full.highlightTerm(highlight)

        assertEquals(2, result.spanStyles.size)
        result.spanStyles.forEach {
            assertEquals("Lor", full.substring(it.start, it.end))
        }
    }
}
