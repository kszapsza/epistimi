package pl.edu.wat.wcy.epistimi.article.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.article.ArticleService
import pl.edu.wat.wcy.epistimi.article.dto.ArticleResponse
import pl.edu.wat.wcy.epistimi.article.dto.ArticlesResponse
import pl.edu.wat.wcy.epistimi.article.dto.toResponse

@RestController
@RequestMapping("/api/article")
class ArticleController(
    private val articleService: ArticleService
) {
    @RequestMapping(
        path = [""],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getArticles(): ResponseEntity<ArticlesResponse> {
        return ResponseEntity.ok(
            ArticlesResponse(
                articleService.getArticles().map { it.toResponse() }
            )
        )
    }

    @RequestMapping(
        path = ["/{slug}"],
        method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getArticleBySlug(@PathVariable slug: String): ResponseEntity<ArticleResponse> =
        ResponseEntity.ok(
            articleService.getArticleBySlug(slug).toResponse()
        )
}
