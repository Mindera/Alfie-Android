package au.com.alfie.ecomm.network.exception

internal enum class ExceptionErrorCodes(val code: Int) {
    HTTP_CLIENT_ERROR_BAD_REQUEST(code = 400),
    HTTP_CLIENT_ERROR_UNAUTHORIZED(code = 401),
    HTTP_CLIENT_ERROR_NOT_FOUND(code = 404),
    HTTP_CLIENT_ERROR_METHOD_NOT_ALLOWED(code = 405),
    HTTP_CLIENT_ERROR_CONFLICT(code = 409),
    HTTP_CLIENT_ERROR_UN_PROCESSABLE_CONTENT(code = 422),
    INTERNAL_HTTP_ERROR(code = 1000)
}
