package au.com.alfie.ecomm.repository.result

data class ErrorResult(
    val type: ErrorType,
    val errorMessage: String? = null,
    val code: String? = null
) : Exception()
