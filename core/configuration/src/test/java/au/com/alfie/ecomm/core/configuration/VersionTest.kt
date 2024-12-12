package au.com.alfie.ecomm.core.configuration

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class VersionTest {

    @Test
    fun testVersionHigher() {
        val versionWithMajor = Version("1")
        val versionWithMinor = Version("1.0")
        val versionWithPatch = Version("1.0.1")

        val testVersions = listOf(
            Version("1.1"),
            Version("1.1.1"),
            Version("2"),
            Version("2.1"),
            Version("2.1.1"),
            Version("3.0.2"),
            Version("1.0.2"),
            Version("1.0.3"),
            Version("1..3"),
            Version("2.00000.3")
        )

        testVersions.forEach {
            assertTrue(it > versionWithMajor)
        }

        testVersions.forEach {
            assertTrue(it > versionWithMinor)
        }

        testVersions.forEach {
            assertTrue(it > versionWithPatch)
        }
    }

    @Test
    fun testVersionEqualOrHigher() {
        val versionWithMajor = Version("1")
        val versionWithMinor = Version("1.1")
        val versionWithPatch = Version("1.1.2")

        assertTrue(versionWithMajor >= Version("1"))
        assertTrue(versionWithMajor >= Version("1.0"))
        assertTrue(versionWithMajor >= Version("1.0.0"))
        assertTrue(versionWithMajor >= Version("0.9"))
        assertTrue(versionWithMajor >= Version("1.0.0"))
        assertTrue(versionWithMajor >= Version("0.9.1"))

        assertTrue(versionWithMinor >= Version("1.1"))
        assertTrue(versionWithMinor >= Version("1.0.2"))
        assertTrue(versionWithMinor >= Version("1"))
        assertTrue(versionWithMinor >= Version("0.9"))

        assertTrue(versionWithPatch >= Version("1.1.2"))
        assertTrue(versionWithPatch >= Version("1"))
        assertTrue(versionWithPatch >= Version("1.1"))
        assertTrue(versionWithPatch >= Version("1.1.1"))
        assertTrue(versionWithPatch >= Version("0.9.9"))
    }

    @Test
    fun testVersionEqual() {
        val versionWithMajor = Version("1")
        val versionWithMinor = Version("1.0")
        val versionWithPatch = Version("1.0.1")

        assertEquals(versionWithMajor, Version("1"))
        assertEquals(versionWithMinor, Version("1.0"))
        assertEquals(versionWithPatch, Version("1.0.1"))

        assertNotEquals(versionWithMajor, Version("1.0"))
        assertNotEquals(versionWithMinor, Version("1.1"))
        assertNotEquals(versionWithPatch, Version("0.1.0"))
    }

    @Test
    fun testVersionEqualOrLower() {
        val versionWithMajor = Version("1")
        val versionWithMinor = Version("1.0")
        val versionWithPatch = Version("1.1.2")

        assertTrue(versionWithMajor <= Version("1"))
        assertTrue(versionWithMajor <= Version("1.1"))
        assertTrue(versionWithMajor <= Version("1.1.2"))
        assertTrue(versionWithMajor <= Version("2"))
        assertTrue(versionWithMajor <= Version("2.6"))
        assertTrue(versionWithMajor <= Version("2.8.9"))

        assertTrue(versionWithMinor <= Version("1.0"))
        assertTrue(versionWithMinor <= Version("1.0.0"))
        assertTrue(versionWithMinor <= Version("1.1"))
        assertTrue(versionWithMinor <= Version("1.1.9"))
        assertTrue(versionWithMinor <= Version("2.0.9"))

        assertTrue(versionWithPatch <= Version("1.1.2"))
        assertTrue(versionWithPatch <= Version("2"))
        assertTrue(versionWithPatch <= Version("2.1"))
        assertTrue(versionWithPatch <= Version("2.1.1"))
        assertTrue(versionWithPatch <= Version("2.9.9"))
    }

    @Test
    fun testVersionLower() {
        val versionWithMajor = Version("4")
        val versionWithMinor = Version("4.0")
        val versionWithPatch = Version("4.2.1")

        val testVersions = listOf(
            Version("1.1"),
            Version("1.1.1"),
            Version("2"),
            Version("2.1"),
            Version("2.1.1"),
            Version("3.0.2"),
            Version("1.0.2"),
            Version("1.0.3"),
            Version("1..3"),
            Version("2.00000.3")
        )

        testVersions.forEach {
            assertTrue(it < versionWithMajor)
        }

        testVersions.forEach {
            assertTrue(it < versionWithMinor)
        }

        testVersions.forEach {
            assertTrue(it < versionWithPatch)
        }
    }
}
