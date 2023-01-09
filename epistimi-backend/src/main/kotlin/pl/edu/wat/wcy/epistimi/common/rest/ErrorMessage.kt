package pl.edu.wat.wcy.epistimi.common.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.time.Instant
import javax.servlet.http.HttpServletRequest

data class ErrorMessage(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val error: String,
    val message: String?,
    val path: String,
) {
    constructor(exception: Exception, httpStatus: HttpStatus, request: WebRequest) : this(
        timestamp = Instant.now(),
        status = httpStatus.value(),
        error = httpStatus.reasonPhrase,
        message = exception.message,
        path = (request as ServletWebRequest).request.requestURI,
    )

    constructor(exception: Exception, httpStatus: HttpStatus, request: HttpServletRequest) : this(
        timestamp = Instant.now(),
        status = httpStatus.value(),
        error = httpStatus.reasonPhrase,
        message = exception.message,
        path = request.requestURI,
    )

    fun toResponseEntity() = ResponseEntity(this, HttpStatus.valueOf(this.status))
}
