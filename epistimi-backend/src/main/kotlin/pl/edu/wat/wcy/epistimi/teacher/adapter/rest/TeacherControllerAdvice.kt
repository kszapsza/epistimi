package pl.edu.wat.wcy.epistimi.teacher.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherNotFoundException
import pl.edu.wat.wcy.epistimi.teacher.domain.TeacherUserIdNotFoundException

@RestControllerAdvice
class TeacherControllerAdvice {

    @ExceptionHandler(
        TeacherNotFoundException::class,
        TeacherUserIdNotFoundException::class,
    )
    fun handleTeacherNotFoundException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()
}
