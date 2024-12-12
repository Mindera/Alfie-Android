package au.com.alfie.ecomm.core.commons.string

import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class StringResourceTest {

    @MockK
    private lateinit var mockContext: Context

    companion object {
        private const val VALUE = "VALUE"
        private const val VALUE_WITH_ARG = "VALUE %s"
        private const val VALUE_WITH_ARGS = "VALUE %1\$s %2\$s"
        private const val VALUE_WITH_DIFFERENT_DATA_TYPES = "VALUE %1\$s %2\$d %3\$s %4\$d"
        private const val ARG = "ARG"
        private const val DUMMY_RESOURCE_ID = 1234
    }

    @BeforeEach
    fun setUp() {
        every { mockContext.getString(any()) } returns VALUE
    }

    @Test
    fun `toString() - GIVEN StringResource is TextResource THEN return text`() {
        val textResource = StringResource.fromText(VALUE)
        assertEquals(textResource.toString(mockContext), VALUE)
    }

    @Test
    fun `toString() - GIVEN StringResource is IdResource Invalid WHEN args is empty THEN return empty String`() {
        val textResource = StringResource.fromId(-1)
        assertEquals(textResource.toString(mockContext), "")
    }

    @Test
    fun `toString() - GIVEN StringResource is IdResource AND args is not empty WHEN no args are of type StringResource THEN return text`() {
        every { mockContext.getString(any()) } returns VALUE_WITH_ARG

        val textResource = StringResource.fromId(DUMMY_RESOURCE_ID, listOf(ARG))
        assertEquals(textResource.toString(mockContext), "$VALUE $ARG")
    }

    @Test
    fun `toString() - GIVEN StringResource is IdResource AND args is not empty WHEN args are of type StringResource THEN return text`() {
        every { mockContext.getString(any()) }.returnsMany(VALUE, VALUE_WITH_ARGS)

        val textResource = StringResource.fromId(
            DUMMY_RESOURCE_ID,
            listOf(ARG, StringResource.fromId(DUMMY_RESOURCE_ID))
        )
        assertEquals(textResource.toString(mockContext), "$VALUE $ARG $VALUE")
    }

    @Test
    fun `toString() - GIVEN StringResource is IdResource AND args is not empty WHEN args are different data types THEN return text with correct format`() {
        every { mockContext.getString(any()) }.returnsMany(VALUE_WITH_DIFFERENT_DATA_TYPES)

        val textResource = StringResource.fromId(DUMMY_RESOURCE_ID, listOf("ARG1", 3, "ARG2", 34))
        assertEquals(textResource.toString(mockContext), "$VALUE ARG1 3 ARG2 34")
    }

    @Test
    fun `toStringOrEmpty() - GIVEN null THEN return empty String`() {
        val textResource: StringResource? = null
        assertEquals(textResource.toStringOrEmpty(mockContext), "")
    }

    @Test
    fun `toStringOrEmpty() - GIVEN StringResource is TextResource THEN return text`() {
        val textResource = StringResource.fromText(VALUE)
        assertEquals(textResource.toStringOrEmpty(mockContext), VALUE)
    }

    @Test
    fun `toStringOrEmpty() - GIVEN StringResource is IdResource WHEN args is empty THEN return text`() {
        val textResource = StringResource.fromId(DUMMY_RESOURCE_ID)
        assertEquals(textResource.toStringOrEmpty(mockContext), VALUE)
    }

    @Test
    fun `toStringOrEmpty() - GIVEN StringResource is IdResource Invalid WHEN args is empty THEN return empty String`() {
        val textResource = StringResource.fromId(-1)
        assertEquals(textResource.toStringOrEmpty(mockContext), "")
    }

    @Test
    fun `toStringOrEmpty() - GIVEN StringResource is IdResource AND args is not empty WHEN no args are of type StringResource THEN return text`() {
        every { mockContext.getString(any()) } returns VALUE_WITH_ARG

        val textResource = StringResource.fromId(DUMMY_RESOURCE_ID, listOf(ARG))
        assertEquals(textResource.toStringOrEmpty(mockContext), "$VALUE $ARG")
    }

    @Test
    fun `toStringOrEmpty() - GIVEN StringResource is TextResource WHEN there is no context THEN return text`() {
        val textResource = StringResource.fromText(VALUE)
        assertEquals(textResource.toStringOrEmpty(), VALUE)
    }

    @Test
    fun `toStringOrEmpty() - GIVEN StringResource is IdResource WHEN there is no context THEN return empty`() {
        val textResource = StringResource.fromId(DUMMY_RESOURCE_ID)
        assertEquals(textResource.toStringOrEmpty(), "")
    }
}
