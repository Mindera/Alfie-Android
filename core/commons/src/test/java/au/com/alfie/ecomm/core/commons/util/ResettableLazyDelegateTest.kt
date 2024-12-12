package au.com.alfie.ecomm.core.commons.util

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ResettableLazyDelegateTest {

    @Test
    fun `value - when calling for the first time then calls initializer`() {
        val initializer: () -> String = mockk()
        every { initializer.invoke() } returns "value"

        val resettableLazy = resettableLazy(initializer)

        val result = resettableLazy.value

        assertEquals("value", result)
        verify { initializer.invoke() }
    }

    @Test
    fun `value - when calling for the second time it does not call initializer`() {
        val initializer: () -> String = mockk()
        every { initializer.invoke() } returns "value"

        val resettableLazy = resettableLazy(initializer)

        resettableLazy.value // First call
        verify(exactly = 1) { initializer.invoke() }

        val result = resettableLazy.value // Second call

        assertEquals("value", result)
        verify(exactly = 1) { initializer.invoke() }
    }

    @Test
    fun `reset - when calling reset then it calls initializer again`() {
        val initializer: () -> String = mockk()
        every { initializer.invoke() } returns "value"

        val resettableLazy = resettableLazy(initializer)

        resettableLazy.value // First call
        verify(exactly = 1) { initializer.invoke() }

        resettableLazy.reset()
        val result = resettableLazy.value // Second call

        assertEquals("value", result)
        verify(exactly = 2) { initializer.invoke() }
    }

    @Test
    fun `isInitialised - when value was not called then returns false`() {
        val resettableLazy = resettableLazy { "value" }

        val result = resettableLazy.isInitialized()

        assertFalse(result)
    }

    @Test
    fun `isInitialised - when value was called then returns false`() {
        val resettableLazy = resettableLazy { "value" }

        resettableLazy.value
        val result = resettableLazy.isInitialized()

        assertTrue(result)
    }

    @Test
    fun `toString - when is initialised then returns the value`() {
        val resettableLazy = resettableLazy { "value" }

        resettableLazy.value
        val result = resettableLazy.toString()

        assertEquals("value", result)
    }

    @Test
    fun `toString - when is not initialised then does not return the value`() {
        val resettableLazy = resettableLazy { "value" }

        val result = resettableLazy.toString()

        assertNotEquals("value", result)
    }
}
