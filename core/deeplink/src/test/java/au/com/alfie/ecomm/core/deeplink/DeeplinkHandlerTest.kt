package au.com.alfie.ecomm.core.deeplink

import app.cash.turbine.test
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkInstance
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkSpec
import au.com.alfie.ecomm.core.deeplink.model.deeplinkSpec
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@ExtendWith(MockKExtension::class)
class DeeplinkHandlerTest {

    @MockK
    private lateinit var matchingSpec: DeeplinkSpec

    @MockK
    private lateinit var nonMatchingSpec: DeeplinkSpec

    @MockK
    private lateinit var deeplinkInstance: DeeplinkInstance

    @BeforeEach
    fun setUp() {
        every { matchingSpec.resolve(any()) } returns deeplinkInstance
        every { nonMatchingSpec.resolve(any()) } returns null
        every { deeplinkInstance.spec } returns matchingSpec
    }

    @Test
    fun `resolve - successfully resolves with only one matching deeplink in interpreter`() = runTest {
        val validResult = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            assertEquals(validResult, expectMostRecentItem())
        }
    }

    @Test
    fun `resolve - successfully resolves with one interpreter that has matching deeplink`() = runTest {
        val validResult = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, matchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            assertEquals(validResult, expectMostRecentItem())
        }
    }

    @Test
    fun `resolve - successfully resolves with matching deeplink in second interpreter`() = runTest {
        val validResult = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, nonMatchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = mockk()
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            assertEquals(validResult, expectMostRecentItem())
        }
    }

    @Test
    fun `resolve - successfully resolves the matching interpreter with more fixed segments`() = runTest {
        val matchingSpec1 = mockk<DeeplinkSpec>()
        val matchingSpec2 = mockk<DeeplinkSpec>()
        val instance1 = mockk<DeeplinkInstance>()
        val instance2 = mockk<DeeplinkInstance>()
        every { matchingSpec1.resolve(any()) } returns instance1
        every { matchingSpec2.resolve(any()) } returns instance2
        every { instance1.spec } returns deeplinkSpec { }
        every { instance2.spec } returns deeplinkSpec {
            appendFixedPathSegment("something")
        }

        val validResult1 = mockk<DeeplinkResult.NavigateTo>()
        val validResult2 = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, matchingSpec1)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult1
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec2)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult2
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            val result = expectMostRecentItem()
            assertNotEquals(validResult1, result)
            assertEquals(validResult2, result)
        }
    }

    @Test
    fun `resolve - successfully resolves the matching interpreter with more pattern segments`() = runTest {
        val matchingSpec1 = mockk<DeeplinkSpec>()
        val matchingSpec2 = mockk<DeeplinkSpec>()
        val instance1 = mockk<DeeplinkInstance>()
        val instance2 = mockk<DeeplinkInstance>()
        every { matchingSpec1.resolve(any()) } returns instance1
        every { matchingSpec2.resolve(any()) } returns instance2
        every { instance1.spec } returns deeplinkSpec {
            appendPatternPathSegment(Regex("something"))
        }
        every { instance2.spec } returns deeplinkSpec {}

        val validResult1 = mockk<DeeplinkResult.NavigateTo>()
        val validResult2 = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, matchingSpec1)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult1
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec2)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult2
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            val result = expectMostRecentItem()
            assertEquals(validResult1, result)
            assertNotEquals(validResult2, result)
        }
    }

    @Test
    fun `resolve - successfully resolves the matching interpreter with more fixed or pattern segments`() = runTest {
        val matchingSpec1 = mockk<DeeplinkSpec>()
        val matchingSpec2 = mockk<DeeplinkSpec>()
        val instance1 = mockk<DeeplinkInstance>()
        val instance2 = mockk<DeeplinkInstance>()
        every { matchingSpec1.resolve(any()) } returns instance1
        every { matchingSpec2.resolve(any()) } returns instance2
        every { instance1.spec } returns deeplinkSpec {
            appendFixedPathSegment("something")
        }
        every { instance2.spec } returns deeplinkSpec {
            appendFixedPathSegment("something1")
            appendPatternPathSegment(Regex("something2"))
        }

        val validResult1 = mockk<DeeplinkResult.NavigateTo>()
        val validResult2 = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, matchingSpec1)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult1
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec2)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult2
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            val result = expectMostRecentItem()
            assertNotEquals(validResult1, result)
            assertEquals(validResult2, result)
        }
    }

    @Test
    fun `resolve - successfully resolves the first matching interpreter when has the same amount of fixed or pattern segments`() = runTest {
        val matchingSpec1 = mockk<DeeplinkSpec>()
        val matchingSpec2 = mockk<DeeplinkSpec>()
        val instance1 = mockk<DeeplinkInstance>()
        val instance2 = mockk<DeeplinkInstance>()
        every { matchingSpec1.resolve(any()) } returns instance1
        every { matchingSpec2.resolve(any()) } returns instance2
        every { instance1.spec } returns deeplinkSpec {
            appendFixedPathSegment("something1")
        }
        every { instance2.spec } returns deeplinkSpec {
            appendPatternPathSegment(Regex("something2"))
        }

        val validResult1 = mockk<DeeplinkResult.NavigateTo>()
        val validResult2 = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, matchingSpec1)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult1
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec2)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult2
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            val result = expectMostRecentItem()
            assertEquals(validResult1, result)
            assertNotEquals(validResult2, result)
        }
    }

    @Test
    fun `resolve - Unresolved when there are no interpreters`() = runTest {
        val interpreters = emptyList<DeeplinkInterpreter>()
        val handler = buildDeeplinkHandler(interpreters)
        val url = "https://www.alfie.com"
        val expected = DeeplinkResult.Unresolved(url)

        handler.deeplinkResult.test {
            handler.handle(url)

            assertEquals(expected, expectMostRecentItem())
        }
    }

    @Test
    fun `resolve - Unresolved when there are no interpreters with matching deeplink`() = runTest {
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec, nonMatchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = mockk()
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(nonMatchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = mockk()
            }
        )
        val handler = buildDeeplinkHandler(interpreters)
        val url = "https://www.alfie.com"
        val expected = DeeplinkResult.Unresolved(url)

        handler.deeplinkResult.test {
            handler.handle(url)

            assertEquals(expected, expectMostRecentItem())
        }
    }

    @Test
    fun `resolve - Unresolved when the matching interpreter returns Unresolved`() = runTest {
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.Unresolved("abc")
            }
        )
        val handler = buildDeeplinkHandler(interpreters)
        val url = "https://www.alfie.com"
        val expected = DeeplinkResult.Unresolved(url)

        handler.deeplinkResult.test {
            handler.handle(url)

            assertEquals(expected, expectMostRecentItem())
        }
    }

    @Test
    fun `resolve - successfully resolves the second matching interpreter when the first returns Unresolved`() = runTest {
        val validResult = mockk<DeeplinkResult.NavigateTo>()
        val interpreters = listOf(
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = DeeplinkResult.Unresolved("abc")
            },
            object : DeeplinkInterpreter {
                override val specs: List<DeeplinkSpec> = listOf(matchingSpec)
                override suspend fun handle(instance: DeeplinkInstance): DeeplinkResult = validResult
            }
        )
        val handler = buildDeeplinkHandler(interpreters)

        handler.deeplinkResult.test {
            handler.handle("https://www.alfie.com")

            val result = expectMostRecentItem()
            assertEquals(validResult, result)
        }
    }

    private fun buildDeeplinkHandler(interpreters: List<DeeplinkInterpreter>) = DeeplinkHandler(
        setOf(
            object : DeeplinkGroup {
                override val interpreters: List<DeeplinkInterpreter> = interpreters
            }
        )
    )
}
