package pl.edu.wat.wcy.epistimi.security.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.security.UnauthorizedException

@RestControllerAdvice
class AuthenticationControllerAdvice {

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.UNAUTHORIZED, request).toResponseEntity()
}
