package au.com.alfie.ecomm.repository.shared.model

data class Size(
    val id: String,
    val description: String?,
    val scale: String?,
    val sizeGuide: SizeGuide?,
    val value: String
)
