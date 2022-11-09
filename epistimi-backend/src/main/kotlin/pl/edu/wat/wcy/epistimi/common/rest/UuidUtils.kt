package pl.edu.wat.wcy.epistimi.common.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.lang.IllegalArgumentException
import java.util.UUID

/**
 * A wrapper around [java.util.UUID.fromString]. Maps generic [java.lang.IllegalArgumentException]
 * into [pl.edu.wat.wcy.epistimi.common.rest.MalformedUuidException], which is globally
 * mapped into HTTP 400 response status.
 *
 * @throws MalformedUuidException if provided string was not a valid UUID.
 */
fun String.toUuid(): UUID = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    throw MalformedUuidException(this)
}

class MalformedUuidException(malformedUuid: String) : Exception("$malformedUuid is not a valid UUID")

@RestControllerAdvice
class UuidMappingControllerAdvice {
    @ExceptionHandler(MalformedUuidException::class)
    fun handleMalformedUuidException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()
}
