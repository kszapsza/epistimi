package pl.edu.wat.wcy.epistimi.shared

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import java.util.Date
import javax.servlet.http.HttpServletRequest

data class ErrorMessage(
    val timestamp: Date = Date(),
    val status: Int,
    val error: String,
    val message: String?,
    val path: String,
) {
    constructor(exception: Exception, httpStatus: HttpStatus, request: WebRequest) : this(
        timestamp = Date(),
        status = httpStatus.value(),
        error = httpStatus.reasonPhrase,
        message = exception.message,
        path = (request as ServletWebRequest).request.requestURI
    )

    constructor(exception: Exception, httpStatus: HttpStatus, request: HttpServletRequest) : this(
        timestamp = Date(),
        status = httpStatus.value(),
        error = httpStatus.reasonPhrase,
        message = exception.message,
        path = request.requestURI
    )

    fun toResponseEntity() = ResponseEntity(this, HttpStatus.valueOf(this.status))
}
