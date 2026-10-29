package com.mindera.alfie.core.ui.extension

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

    @Test
    fun `Regex metacharacters in the term are matched literally instead of throwing`() {
        listOf("shirt(", "[", "+", "a*b", "\\").forEach { term ->
            assertEquals(0, "Plain shirt".highlightTerm(term).spanStyles.size, "term=$term")
        }
        val result = "T-shirt (navy)".highlightTerm("shirt (")

        assertEquals(1, result.spanStyles.size)
        assertEquals("shirt (", "T-shirt (navy)".substring(result.spanStyles[0].start, result.spanStyles[0].end))
    }

    @Test
    fun `A dot highlights only dots, not every character`() {
        val result = "Sole & Co.".highlightTerm(".")

        assertEquals(1, result.spanStyles.size)
        assertEquals(9, result.spanStyles[0].start)
    }

    @Test
    fun `The match ignores case`() {
        val result = "SKIMS".highlightTerm("sk")

        assertEquals("SK", "SKIMS".substring(result.spanStyles[0].start, result.spanStyles[0].end))
    }

    @Test
    fun `An empty term highlights nothing`() {
        assertEquals(0, "Lorem".highlightTerm("").spanStyles.size)
    }
}
