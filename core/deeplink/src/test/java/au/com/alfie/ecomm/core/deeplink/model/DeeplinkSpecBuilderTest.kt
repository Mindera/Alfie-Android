package au.com.alfie.ecomm.core.deeplink.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeeplinkSpecBuilderTest {

    @Test
    fun `deeplinkSpec - with empty builder then create spec without path segments and query parameters`() {
        val result = deeplinkSpec { }

        assertTrue(result.pathSegments.isEmpty())
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendPathSegment adds to the pathSegments`() {
        val segment = DeeplinkPathSegment.Fixed("something")

        val result = deeplinkSpec {
            appendPathSegment(segment)
        }

        assertEquals(1, result.pathSegments.size)
        assertEquals(segment, result.pathSegments[0])
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendFixedPathSegment adds to the pathSegments`() {
        val value = "something"
        val segment = DeeplinkPathSegment.Fixed(value)

        val result = deeplinkSpec {
            appendFixedPathSegment(value)
        }

        assertEquals(1, result.pathSegments.size)
        assertEquals(segment, result.pathSegments[0])
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendPatternPathSegment adds to the pathSegments`() {
        val pattern = Regex("something.")
        val segment = DeeplinkPathSegment.Pattern(pattern)

        val result = deeplinkSpec {
            appendPatternPathSegment(pattern)
        }

        assertEquals(1, result.pathSegments.size)
        assertEquals(segment, result.pathSegments[0])
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendArgumentPathSegment adds to the pathSegments`() {
        val argumentName = "id"
        val segment = DeeplinkPathSegment.Argument(argumentName)

        val result = deeplinkSpec {
            appendArgumentPathSegment(argumentName)
        }

        assertEquals(1, result.pathSegments.size)
        assertEquals(segment, result.pathSegments[0])
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendPathSegments adds to the pathSegments`() {
        val segment1 = DeeplinkPathSegment.Fixed("something")
        val segment2 = DeeplinkPathSegment.Pattern(Regex("something."))
        val segment3 = DeeplinkPathSegment.Argument("id")

        val result = deeplinkSpec {
            appendPathSegments(segment1, segment2, segment3)
        }

        assertEquals(3, result.pathSegments.size)
        assertEquals(segment1, result.pathSegments[0])
        assertEquals(segment2, result.pathSegments[1])
        assertEquals(segment3, result.pathSegments[2])
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendPathSegments with list adds to the pathSegments`() {
        val segment1 = DeeplinkPathSegment.Fixed("something")
        val segment2 = DeeplinkPathSegment.Pattern(Regex("something."))
        val segment3 = DeeplinkPathSegment.Argument("id")

        val result = deeplinkSpec {
            appendPathSegments(listOf(segment1, segment2, segment3))
        }

        assertEquals(3, result.pathSegments.size)
        assertEquals(segment1, result.pathSegments[0])
        assertEquals(segment2, result.pathSegments[1])
        assertEquals(segment3, result.pathSegments[2])
        assertTrue(result.queryParameters.isEmpty())
    }

    @Test
    fun `deeplinkSpec - appendQueryParameter adds to the queryParameters`() {
        val result = deeplinkSpec {
            appendQueryParameter("name")
        }

        assertTrue(result.pathSegments.isEmpty())
        assertEquals(1, result.queryParameters.size)
        assertEquals("name", result.queryParameters[0])
    }

    @Test
    fun `deeplinkSpec - appendQueryParameters adds to the queryParameters`() {
        val result = deeplinkSpec {
            appendQueryParameters("name", "desc")
        }

        assertTrue(result.pathSegments.isEmpty())
        assertEquals(2, result.queryParameters.size)
        assertEquals("name", result.queryParameters[0])
        assertEquals("desc", result.queryParameters[1])
    }

    @Test
    fun `deeplinkSpec - appendQueryParameters with list adds to the queryParameters`() {
        val result = deeplinkSpec {
            appendQueryParameters(listOf("name", "desc"))
        }

        assertTrue(result.pathSegments.isEmpty())
        assertEquals(2, result.queryParameters.size)
        assertEquals("name", result.queryParameters[0])
        assertEquals("desc", result.queryParameters[1])
    }

    @Test
    fun `deeplinkSpec - append path segments and query parameters`() {
        val segment1 = DeeplinkPathSegment.Fixed("something")
        val segment2 = DeeplinkPathSegment.Pattern(Regex("something."))
        val segment3 = DeeplinkPathSegment.Argument("id")

        val result = deeplinkSpec {
            appendPathSegment(segment1)
            appendQueryParameter("name")
            appendPathSegment(segment2)
            appendQueryParameter("desc")
            appendPathSegment(segment3)
        }

        assertEquals(3, result.pathSegments.size)
        assertEquals(segment1, result.pathSegments[0])
        assertEquals(segment2, result.pathSegments[1])
        assertEquals(segment3, result.pathSegments[2])
        assertEquals(2, result.queryParameters.size)
        assertEquals("name", result.queryParameters[0])
        assertEquals("desc", result.queryParameters[1])
    }
}
