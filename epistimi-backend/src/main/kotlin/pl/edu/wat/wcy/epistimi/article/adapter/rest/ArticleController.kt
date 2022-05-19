package pl.edu.wat.wcy.epistimi.article.adapter.rest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.article.ArticleFacade
import pl.edu.wat.wcy.epistimi.common.api.MediaType
import pl.edu.wat.wcy.epistimi.common.mapper.RestHandlers

@RestController
@RequestMapping("/api/article")
@Tag(name = "article", description = "API for retrieving and managing articles from Epistimi homepage")
class ArticleController(
    private val articleFacade: ArticleFacade,
) {
    @Operation(
        summary = "Get all articles",
        tags = ["article"],
        description = "Retrieves a list of all articles",
    )
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

    @Operation(
        summary = "Get article by slug",
        tags = ["article"],
        description = "Retrieves an article with provided slug",
    )
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
