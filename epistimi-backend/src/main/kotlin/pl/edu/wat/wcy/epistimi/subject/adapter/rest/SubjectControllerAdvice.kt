package pl.edu.wat.wcy.epistimi.subject.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.subject.domain.SubjectBadRequestException

@RestControllerAdvice
class SubjectControllerAdvice {

    @ExceptionHandler(SubjectBadRequestException::class)
    fun handleSubjectRegisterBadRequestException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()
}
