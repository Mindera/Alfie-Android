package au.com.alfie.ecomm.core.deeplink.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DeeplinkSpecTest {

    @Test
    fun testResolveEmptySpecSuccessfully() {
        val spec = deeplinkSpec { }

        val result = spec.resolve("https://www.alfie.com")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveEmptySpecSuccessfullyWithTrailingSlash() {
        val spec = deeplinkSpec { }

        val result = spec.resolve("https://www.alfie.com/")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveEmptySpecUnsuccessfullyWithUnexpectedPathSegment() {
        val spec = deeplinkSpec { }

        val result = spec.resolve("https://www.alfie.com/something")

        assertNull(result)
    }

    @Test
    fun testResolveSpecUnsuccessfullyWithWrongNumberOfPathSegments() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("something1")
            appendFixedPathSegment("something2")
        }

        val result = spec.resolve("https://www.alfie.com/something1")

        assertNull(result)
    }

    @Test
    fun testResolveSpecSuccessfullyWithCorrectNumberOfPathSegments() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("something1")
            appendFixedPathSegment("something2")
        }

        val result = spec.resolve("https://www.alfie.com/something1/something2")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesFixedPathSegment() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("something1")
        }

        val result = spec.resolve("https://www.alfie.com/something1")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesFixedPathSegmentWithTrailingSlash() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("something1")
        }

        val result = spec.resolve("https://www.alfie.com/something1/")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesFixedPathSegment() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("something1")
        }

        val result = spec.resolve("https://www.alfie.com/something2")

        assertNull(result)
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesPatternPathSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("something."))
        }

        val result = spec.resolve("https://www.alfie.com/something1")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesPatternPathSegmentWithTrailingSlash() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("something."))
        }

        val result = spec.resolve("https://www.alfie.com/something2/")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesPatternPathSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("something."))
        }

        val result = spec.resolve("https://www.alfie.com/something")

        assertNull(result)
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesArgumentPathSegment() {
        val spec = deeplinkSpec {
            appendArgumentPathSegment("id")
        }

        val result = spec.resolve("https://www.alfie.com/123")

        assertNotNull(result)
        assertEquals(1, result.pathArguments.size)
        assertEquals("123", result.pathArguments["id"])
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesArgumentPathSegmentWithTrailingSlash() {
        val spec = deeplinkSpec {
            appendArgumentPathSegment("id")
        }

        val result = spec.resolve("https://www.alfie.com/123/")

        assertNotNull(result)
        assertEquals(1, result.pathArguments.size)
        assertEquals("123", result.pathArguments["id"])
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesMultipleArgumentPathSegments() {
        val spec = deeplinkSpec {
            appendArgumentPathSegment("id")
            appendArgumentPathSegment("name")
        }

        val result = spec.resolve("https://www.alfie.com/123/something")

        assertNotNull(result)
        assertEquals(2, result.pathArguments.size)
        assertEquals("123", result.pathArguments["id"])
        assertEquals("something", result.pathArguments["name"])
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesArgumentPathSegmentsWithCorrectDecoding() {
        val spec = deeplinkSpec {
            appendArgumentPathSegment("name")
            appendArgumentPathSegment("desc")
        }

        val result = spec.resolve("https://www.alfie.com/something%20big/Something%20Really%20Big")

        assertNotNull(result)
        assertEquals(2, result.pathArguments.size)
        assertEquals("something big", result.pathArguments["name"])
        assertEquals("Something Really Big", result.pathArguments["desc"])
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesArgumentPathSegmentWithCorrectPattern() {
        val spec = deeplinkSpec {
            appendArgumentPathSegment(
                argumentName = "id",
                pattern = Regex("[0-9]{3}") // Three digits
            )
        }

        val result = spec.resolve("https://www.alfie.com/123")

        assertNotNull(result)
        assertEquals(1, result.pathArguments.size)
        assertEquals("123", result.pathArguments["id"])
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesArgumentPathSegmentWithWrongPattern() {
        val spec = deeplinkSpec {
            appendArgumentPathSegment(
                argumentName = "id",
                pattern = Regex("[0-9]{3}") // Three digits
            )
        }

        val result = spec.resolve("https://www.alfie.com/12345")

        assertNull(result)
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesDifferentTypesOfPathSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("(wo)?men"))
            appendFixedPathSegment("product")
            appendArgumentPathSegment("id")
        }

        val result = spec.resolve("https://www.alfie.com/women/product/123")

        assertNotNull(result)
        assertEquals(1, result.pathArguments.size)
        assertEquals("123", result.pathArguments["id"])
        assertTrue(result.queryArguments.isEmpty())
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesDifferentTypesOfPathSegmentWithWrongPatternSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("(wo)?men"))
            appendFixedPathSegment("product")
            appendArgumentPathSegment("id")
        }

        val result = spec.resolve("https://www.alfie.com/womenx/product/123")

        assertNull(result)
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesDifferentTypesOfPathSegmentWithWrongFixedSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("(wo)?men"))
            appendFixedPathSegment("product")
            appendArgumentPathSegment("id")
        }

        val result = spec.resolve("https://www.alfie.com/women/xproductx/123")

        assertNull(result)
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesDifferentTypesOfPathSegmentWithWrongArgumentSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("(wo)?men"))
            appendFixedPathSegment("product")
            appendArgumentPathSegment(
                argumentName = "id",
                pattern = Regex("[0-9]*") // Only digits
            )
        }

        val result = spec.resolve("https://www.alfie.com/women/product/123abc")

        assertNull(result)
    }

    @Test
    fun testResolveSpecUnsuccessfullyMatchesDifferentTypesOfPathSegmentWithoutArgumentSegment() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("(wo)?men"))
            appendFixedPathSegment("product")
            appendArgumentPathSegment("id")
        }

        val result = spec.resolve("https://www.alfie.com/women/product")

        assertNull(result)
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesQueryParameter() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("list")
            appendQueryParameter("color")
        }

        val result = spec.resolve("https://www.alfie.com/list?color=green")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(1, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesMultipleQueryParameters() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("list")
            appendQueryParameters("color", "size")
        }

        val result = spec.resolve("https://www.alfie.com/list?color=green&size=xxl")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(2, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
        assertEquals("xxl", result.queryArguments["size"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesMultipleQueryParametersInDifferentOrder() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("list")
            appendQueryParameters("color", "size")
        }

        val result = spec.resolve("https://www.alfie.com/list?size=xxl&color=green")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(2, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
        assertEquals("xxl", result.queryArguments["size"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesQueryParametersWithoutUnspecifiedParameter() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("list")
            appendQueryParameter("color")
        }

        val result = spec.resolve("https://www.alfie.com/list?color=green&size=xxl")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(1, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
        assertNull(result.queryArguments["size"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesQueryParametersWithLeadingSlash() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("list")
            appendQueryParameter("color")
        }

        val result = spec.resolve("https://www.alfie.com/list/?color=green")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(1, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesQueryParametersWithoutPathSegments() {
        val spec = deeplinkSpec {
            appendQueryParameter("color")
        }

        val result = spec.resolve("https://www.alfie.com/?color=green")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(1, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesQueryParametersWithCorrectDecoding() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("list")
            appendQueryParameters("color", "size")
        }

        val result = spec.resolve("https://www.alfie.com/list?color=light%20green&size=Ultra+Large")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertEquals(2, result.queryArguments.size)
        assertEquals("light green", result.queryArguments["color"])
        assertEquals("Ultra Large", result.queryArguments["size"])
    }

    @Test
    fun testResolveSpecSuccessfullyMatchesDifferentTypesOfPathSegmentAndQueryParameters() {
        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("(wo)?men"))
            appendFixedPathSegment("product")
            appendArgumentPathSegment("id")
            appendQueryParameters("color", "size")
        }

        val result = spec.resolve("https://www.alfie.com/women/product/123?color=green&size=xxl")

        assertNotNull(result)
        assertEquals(1, result.pathArguments.size)
        assertEquals("123", result.pathArguments["id"])
        assertEquals(2, result.queryArguments.size)
        assertEquals("green", result.queryArguments["color"])
        assertEquals("xxl", result.queryArguments["size"])
    }

    @Test
    fun testResolveSpecSpecSuccessfullyParsingFragment() {
        val spec = deeplinkSpec {
            appendFixedPathSegment("brand")
        }

        val result = spec.resolve("https://www.alfie.com/brand#A")

        assertNotNull(result)
        assertTrue(result.pathArguments.isEmpty())
        assertTrue(result.queryArguments.isEmpty())
        assertEquals("A", result.namedAnchor)
    }
}
