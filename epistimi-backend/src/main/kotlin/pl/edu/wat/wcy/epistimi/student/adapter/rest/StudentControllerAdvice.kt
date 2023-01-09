package pl.edu.wat.wcy.epistimi.student.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.student.domain.StudentNotFoundException

@RestControllerAdvice
class StudentControllerAdvice {
    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFoundException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()
}
