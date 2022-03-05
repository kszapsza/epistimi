package pl.edu.wat.wcy.epistimi.user.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UsernameAlreadyInUseException
import pl.edu.wat.wcy.epistimi.shared.api.ErrorMessage

@RestControllerAdvice
class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()

    @ExceptionHandler(UsernameAlreadyInUseException::class)
    fun handleRegistrationBadRequestExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()
}
