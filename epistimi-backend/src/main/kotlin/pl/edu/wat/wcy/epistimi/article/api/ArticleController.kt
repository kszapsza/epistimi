package pl.edu.wat.wcy.epistimi.article.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.wat.wcy.epistimi.article.Article
import pl.edu.wat.wcy.epistimi.article.ArticleService
import pl.edu.wat.wcy.epistimi.article.dto.ArticleResponse
import pl.edu.wat.wcy.epistimi.article.dto.ArticlesResponse

@RestController
@RequestMapping("/api/article")
class ArticleController(
    private val articleService: ArticleService
) {

    @GetMapping
    fun getArticles(): ResponseEntity<ArticlesResponse> =
        ResponseEntity.ok(
            ArticlesResponse(
                articleService.getArticles()
                    .map { it.toResponse() }
            )
        )

    private fun Article.toResponse() = ArticleResponse(
        id = this.id,
        slug = this.slug,
        title = this.title,
        description = this.description,
    )
}
