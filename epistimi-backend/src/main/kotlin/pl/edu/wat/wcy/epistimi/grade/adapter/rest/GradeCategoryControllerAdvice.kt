package pl.edu.wat.wcy.epistimi.grade.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategoryNotFoundException
import pl.edu.wat.wcy.epistimi.grade.domain.GradeCategorySubjectNotFoundException

@RestControllerAdvice(basePackages = ["pl.edu.wat.wcy.epistimi.grade"])
class GradeCategoryControllerAdvice {
    @ExceptionHandler(
        GradeCategorySubjectNotFoundException::class,
        GradeCategoryNotFoundException::class,
    )
    fun handleGradeCategoryNotFoundException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()
}
