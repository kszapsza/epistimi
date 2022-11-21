package pl.edu.wat.wcy.epistimi.grade.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryActionForbiddenException
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIllegalFiltersException
import pl.edu.wat.wcy.epistimi.grade.domain.GradeIssueForbiddenException

@RestControllerAdvice(basePackages = ["pl.edu.wat.wcy.epistimi.grade"])
class GradeControllerAdvice {
    @ExceptionHandler(GradeIllegalFiltersException::class)
    fun handleGradeBadRequestExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()

    @ExceptionHandler(
        GradeCategoryActionForbiddenException::class,
        GradeIssueForbiddenException::class,
    )
    fun handleGradeForbiddenExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.FORBIDDEN, request).toResponseEntity()
}
