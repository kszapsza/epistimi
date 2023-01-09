package pl.edu.wat.wcy.epistimi.article.adapter.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import pl.edu.wat.wcy.epistimi.article.domain.ArticleNotFoundException
import pl.edu.wat.wcy.epistimi.common.rest.ErrorMessage

@RestControllerAdvice
class ArticleControllerAdvice {

    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFoundExceptions(exception: Exception, request: WebRequest) =
        ErrorMessage(exception, HttpStatus.NOT_FOUND, request).toResponseEntity()
}
