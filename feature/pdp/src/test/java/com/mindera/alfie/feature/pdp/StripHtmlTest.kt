package com.mindera.alfie.feature.pdp

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * [stripHtml] delegates to `HtmlCompat.fromHtml`, which requires the Android framework, so this
 * runs under Robolectric (JUnit4, executed via the junit-vintage engine).
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [34])
class StripHtmlTest {

    @Test
    fun `decodes named entities`() {
        assertEquals("Tom & Jerry", "Tom &amp; Jerry".stripHtml())
    }

    @Test
    fun `decodes comparison and currency entities`() {
        assertEquals("£10 < 20 > 5", "&pound;10 &lt; 20 &gt; 5".stripHtml())
    }

    @Test
    fun `normalises nbsp to a regular space within text`() {
        assertEquals("a b", "a&nbsp;b".stripHtml())
    }

    @Test
    fun `strips block tags`() {
        assertEquals("Hello", "<p>Hello</p>".stripHtml())
    }

    @Test
    fun `converts br to a newline`() {
        assertEquals("Line1\nLine2", "Line1<br/>Line2".stripHtml())
    }

    @Test
    fun `blank input returns empty`() {
        assertEquals("", "".stripHtml())
        assertEquals("", (null as String?).stripHtml())
        assertEquals("", "   ".stripHtml())
    }
}
