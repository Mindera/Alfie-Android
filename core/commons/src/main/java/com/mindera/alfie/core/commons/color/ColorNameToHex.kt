package com.mindera.alfie.core.commons.color

/**
 * Static lookup of common color names to hex values, used as a fallback for product swatches when
 * the BFF doesn't expose a swatch image. Matching is case- and whitespace-insensitive.
 *
 * TODO(ALFMOB-388): replace with BFF-driven swatch images once the new BFF surfaces them.
 */
object ColorNameToHex {

    private val map: Map<String, Long> = mapOf(
        "black" to 0xFF000000,
        "white" to 0xFFFFFFFF,
        "ivory" to 0xFFFFFFF0,
        "cream" to 0xFFFFFDD0,
        "beige" to 0xFFF5F5DC,
        "tan" to 0xFFD2B48C,
        "brown" to 0xFF8B4513,
        "chocolate" to 0xFF5C3317,
        "grey" to 0xFF808080,
        "gray" to 0xFF808080,
        "silver" to 0xFFC0C0C0,
        "charcoal" to 0xFF36454F,
        "navy" to 0xFF000080,
        "blue" to 0xFF1E3A8A,
        "teal" to 0xFF008080,
        "green" to 0xFF065F46,
        "olive" to 0xFF808000,
        "khaki" to 0xFFC3B091,
        "yellow" to 0xFFFCD34D,
        "gold" to 0xFFD4AF37,
        "orange" to 0xFFEA580C,
        "red" to 0xFFB91C1C,
        "burgundy" to 0xFF800020,
        "pink" to 0xFFEC4899,
        "purple" to 0xFF6B21A8,
        "lilac" to 0xFFC8A2C8
    )

    fun lookup(name: String?): Long? {
        if (name.isNullOrBlank()) return null
        val normalised = name.trim().lowercase().replace(Regex("\\s+"), " ")
        map[normalised]?.let { return it }
        // Try last token (e.g. "Light Blue" → "blue") to absorb common modifiers.
        val lastToken = normalised.substringAfterLast(' ')
        return map[lastToken]
    }
}
