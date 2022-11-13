package pl.edu.wat.wcy.epistimi.article

import pl.edu.wat.wcy.epistimi.article.domain.Article
import pl.edu.wat.wcy.epistimi.article.domain.port.ArticleRepository

class ArticleFacade(
    private val articleRepository: ArticleRepository,
) {
    fun getArticles(): List<Article> = articleRepository.findAll()
    fun getArticleBySlug(slug: String): Article = articleRepository.findBySlug(slug)
}
