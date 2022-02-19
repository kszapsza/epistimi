package pl.edu.wat.wcy.epistimi.article.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pl.edu.wat.wcy.epistimi.article.ArticleNotFoundException

@ControllerAdvice
class ArticleControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArticleNotFoundException::class)
    fun handleArticleNotFoundExceptions() {
        // TODO
    }

}