package pl.edu.wat.wcy.epistimi.article.adapter.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.article.ArticleFacade
import pl.edu.wat.wcy.epistimi.shared.api.MediaType
import pl.edu.wat.wcy.epistimi.shared.mapper.RestHandlers

@RestController
@RequestMapping("/api/article")
class ArticleController(
    private val articleFacade: ArticleFacade,
) {
    @GetMapping(
        path = [""],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getArticles(): ResponseEntity<ArticlesResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(
                mapper = ArticlesResponseMapper,
                logic = articleFacade::getArticles,
            )
        )
    }

    @GetMapping(
        path = ["/{slug}"],
        produces = [MediaType.APPLICATION_JSON_V1]
    )
    fun getArticleBySlug(@PathVariable slug: String): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(
            RestHandlers.handleRequest(
                mapper = ArticleResponseMapper,
                logic = { articleFacade.getArticleBySlug(slug) }
            )
        )
    }
}
