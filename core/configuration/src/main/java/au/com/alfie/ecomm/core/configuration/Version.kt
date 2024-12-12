package au.com.alfie.ecomm.core.configuration

import au.com.alfie.ecomm.core.commons.extension.toIntOrZero

private const val VERSION_DELIMITER = "."
private const val RESULT_LESS_THAN = -1
private const val RESULT_EQUAL_TO = 0
private const val RESULT_GREATER_THAN = 1
private const val MAJOR_VERSION_INDEX = 0
private const val MINOR_VERSION_INDEX = 1
private const val PATCH_VERSION_INDEX = 2

data class Version(val version: String)

/**
 * Compares this value with the specified value for order. Returns zero if this value is equal to the specified other value,
 * a negative number if it's less than other, or a positive number if it's greater than other.
 */
internal infix operator fun Version.compareTo(other: Version): Int {
    val versionParts = this.version.split(VERSION_DELIMITER)
    val otherVersionParts = other.version.split(VERSION_DELIMITER)

    val versionMajor = versionParts.getOrNull(index = MAJOR_VERSION_INDEX).toIntOrZero()
    val versionMinor = versionParts.getOrNull(index = MINOR_VERSION_INDEX).toIntOrZero()
    val versionPatch = versionParts.getOrNull(index = PATCH_VERSION_INDEX).toIntOrZero()

    val otherVersionMajor = otherVersionParts.getOrNull(index = MAJOR_VERSION_INDEX).toIntOrZero()
    val otherVersionMinor = otherVersionParts.getOrNull(index = MINOR_VERSION_INDEX).toIntOrZero()
    val otherVersionPatch = otherVersionParts.getOrNull(index = PATCH_VERSION_INDEX).toIntOrZero()

    val majorComparisonResult = versionMajor.compareTo(otherVersionMajor)
    val minorComparisonResult = versionMinor.compareTo(otherVersionMinor)
    val patchComparisonResult = versionPatch.compareTo(otherVersionPatch)

    return when {
        majorComparisonResult == RESULT_EQUAL_TO && minorComparisonResult == RESULT_EQUAL_TO && patchComparisonResult == RESULT_EQUAL_TO -> RESULT_EQUAL_TO
        majorComparisonResult == RESULT_LESS_THAN -> RESULT_LESS_THAN
        majorComparisonResult == RESULT_EQUAL_TO && minorComparisonResult == RESULT_LESS_THAN -> RESULT_LESS_THAN
        majorComparisonResult == RESULT_EQUAL_TO && minorComparisonResult == RESULT_EQUAL_TO && patchComparisonResult == RESULT_LESS_THAN -> RESULT_LESS_THAN
        majorComparisonResult == RESULT_GREATER_THAN || minorComparisonResult == RESULT_GREATER_THAN || patchComparisonResult == RESULT_GREATER_THAN -> RESULT_GREATER_THAN
        else -> RESULT_LESS_THAN
    }
}
