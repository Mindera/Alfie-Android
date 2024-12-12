package au.com.alfie.ecomm.repository.shared.model

data class Pagination(
    val limit: Int,
    val offset: Int,
    val page: Int,
    val pageCount: Int,
    val total: Int,
    val nextPage: Int?,
    val previousPage: Int?
)
