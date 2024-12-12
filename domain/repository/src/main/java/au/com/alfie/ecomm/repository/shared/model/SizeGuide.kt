package au.com.alfie.ecomm.repository.shared.model

data class SizeGuide(
    val id: String,
    val description: String?,
    val name: String,
    val sizes: List<Size>
)
