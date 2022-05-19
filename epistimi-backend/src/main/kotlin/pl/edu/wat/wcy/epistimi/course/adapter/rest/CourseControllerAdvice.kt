package pl.edu.wat.wcy.epistimi.course.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.course.CourseBadRequestException
import pl.edu.wat.wcy.epistimi.course.CourseNotFoundException
import pl.edu.wat.wcy.epistimi.course.CourseUnmodifiableException
import pl.edu.wat.wcy.epistimi.common.api.ErrorMessage

@RestControllerAdvice
class CourseControllerAdvice {

    @ExceptionHandler(CourseNotFoundException::class)
    fun handleCourseNotFoundException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()

    @ExceptionHandler(
        CourseBadRequestException::class,
        CourseUnmodifiableException::class,
    )
    fun handleCourseBadRequestException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.BAD_REQUEST, request).toResponseEntity()
}
