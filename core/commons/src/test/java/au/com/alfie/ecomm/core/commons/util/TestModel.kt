package au.com.alfie.ecomm.core.commons.util

internal data class TestModel(
    val isEnabled: Boolean,
    val label: String,
    val position: Int,
    val attributes: List<String>
)
