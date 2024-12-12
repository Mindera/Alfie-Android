package au.com.alfie.ecomm.repository.result

enum class ErrorType {
    GENERIC_ERROR,
    UNKNOWN,
    NETWORK,
    RESOURCE_NOT_FOUND,
    AUTHENTICATION_FAILED,
    CONFLICT,
    INVALID_REQUEST,
    BAD_REQUEST,
    UN_PROCESSABLE_ENTITY,
    METHOD_NOT_ALLOWED
}
