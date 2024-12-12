package au.com.alfie.ecomm.core.deeplink.model

import android.net.Uri
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(MockKExtension::class)
class DeeplinkSpecUnitTest {

    @MockK
    private lateinit var uri: Uri

    @BeforeEach
    fun setUp() {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns uri
    }

    @Test
    fun `resolve - non-hierarchical URI returns null`() {
        setupUri(isHierarchical = false)

        val spec = deeplinkSpec { }

        val result = spec.resolve("invalid_url")

        assertNull(result)
    }

    @Test
    fun `resolve - spec with different number of path segments returns null`() {
        setupUri(pathSegments = listOf("path1", "path2", "path3"))

        val spec = deeplinkSpec {
            appendFixedPathSegment("path1")
            appendFixedPathSegment("path2")
        }

        val result = spec.resolve("https://www.alfie.com/path1/path2/path3")

        assertNull(result)
    }

    @Test
    fun `resolve - spec with same number of path segments returns not null`() {
        setupUri(pathSegments = listOf("path1", "path2", "path3"))

        val spec = deeplinkSpec {
            appendFixedPathSegment("path1")
            appendFixedPathSegment("path2")
            appendFixedPathSegment("path3")
        }

        val result = spec.resolve("https://www.alfie.com/path1/path2/path3")

        assertNotNull(result)
    }

    @Test
    fun `resolve - when fixed segment does not match then returns null`() {
        setupUri(pathSegments = listOf("path"))

        val spec = deeplinkSpec {
            appendFixedPathSegment("path123")
        }

        val result = spec.resolve("https://www.alfie.com/path")

        assertNull(result)
    }

    @Test
    fun `resolve - when pattern segment does not match then returns null`() {
        setupUri(pathSegments = listOf("path1"))

        val spec = deeplinkSpec {
            appendPatternPathSegment(Regex("path[0-9]{3}"))
        }

        val result = spec.resolve("https://www.alfie.com/path1")

        assertNull(result)
    }

    @Test
    fun `resolve - when argument segment with pattern validation does not match then returns null`() {
        setupUri(pathSegments = listOf("path1"))

        val spec = deeplinkSpec {
            appendArgumentPathSegment(
                argumentName = "arg",
                pattern = Regex("path[0-9]{3}")
            )
        }

        val result = spec.resolve("https://www.alfie.com/path1")

        assertNull(result)
    }

    @Test
    fun `resolve - when path segments match then returns not null`() {
        setupUri(pathSegments = listOf("path", "path1", "path123"))

        val spec = deeplinkSpec {
            appendFixedPathSegment("path")
            appendPatternPathSegment(Regex("path[0-9]"))
            appendArgumentPathSegment(
                argumentName = "arg",
                pattern = Regex("path[0-9]{3}")
            )
        }

        val result = spec.resolve("https://www.alfie.com/path/path1/path123")

        assertNotNull(result)
        assertEquals(spec, result.spec)
    }

    @Test
    fun `resolve - argument path segments are correct on the return instance`() {
        setupUri(pathSegments = listOf("path1", "path2", "path3"))

        val spec = deeplinkSpec {
            appendArgumentPathSegment(
                argumentName = "arg1",
                pattern = Regex("path[0-9]")
            )
            appendArgumentPathSegment("arg2")
            appendArgumentPathSegment("arg3")
        }

        val result = spec.resolve("https://www.alfie.com/path1/path2/path3")

        assertNotNull(result)
        assertEquals("path1", result.pathArguments["arg1"])
        assertEquals("path2", result.pathArguments["arg2"])
        assertEquals("path3", result.pathArguments["arg3"])
    }

    @Test
    fun `resolve - query parameter are correct on the return instance`() {
        setupUri(
            queryParameters = mapOf(
                "query1" to "value1",
                "query2" to "value2",
                "query3" to "value3"
            )
        )

        val spec = deeplinkSpec {
            appendQueryParameters("query1", "query2", "query3")
        }

        val result = spec.resolve("https://www.alfie.com/?query1=value1&query2=value2&query3=value3")

        assertNotNull(result)
        assertEquals("value1", result.queryArguments["query1"])
        assertEquals("value2", result.queryArguments["query2"])
        assertEquals("value3", result.queryArguments["query3"])
    }

    @Test
    fun `resolve - fragment is correct on the return instance`() {
        setupUri(
            fragment = "abc",
            pathSegments = listOf("path")
        )

        val spec = deeplinkSpec {
            appendFixedPathSegment("path")
        }

        val result = spec.resolve("https://www.alfie.com/path#abc")

        assertNotNull(result)
        assertEquals("abc", result.namedAnchor)
    }

    @Test
    fun `resolve - when has no fragment then namedAnchor is null on the return instance`() {
        setupUri(
            fragment = null,
            pathSegments = listOf("path")
        )

        val spec = deeplinkSpec {
            appendFixedPathSegment("path")
        }

        val result = spec.resolve("https://www.alfie.com/path")

        assertNotNull(result)
        assertNull(result.namedAnchor)
    }

    private fun setupUri(
        isHierarchical: Boolean = true,
        pathSegments: List<String> = emptyList(),
        queryParameters: Map<String, String> = emptyMap(),
        fragment: String? = null
    ) {
        every { uri.isHierarchical } returns isHierarchical
        every { uri.pathSegments } returns pathSegments
        every { uri.getQueryParameter(any()) } returns null
        queryParameters.forEach { key, value ->
            every { uri.getQueryParameter(key) } returns value
        }
        every { uri.fragment } returns fragment
    }
}
