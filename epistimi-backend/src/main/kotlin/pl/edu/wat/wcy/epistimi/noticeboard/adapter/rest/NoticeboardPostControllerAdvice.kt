package pl.edu.wat.wcy.epistimi.noticeboard.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostActionForbidden
import pl.edu.wat.wcy.epistimi.noticeboard.NoticeboardPostNotFoundException

@RestControllerAdvice
class NoticeboardPostControllerAdvice {

    @ExceptionHandler(NoticeboardPostNotFoundException::class)
    fun handleNoticeboardPostNotFoundException(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()

    @ExceptionHandler(NoticeboardPostActionForbidden::class)
    fun handleNoticeboardPostActionForbidden(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.FORBIDDEN, request).toResponseEntity()
}
