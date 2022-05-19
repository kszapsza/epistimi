package pl.edu.wat.wcy.epistimi.article.port

import pl.edu.wat.wcy.epistimi.article.Article

interface ArticleRepository {
    fun findAll(): List<Article>
    fun findById(articleId: String): Article
    fun findBySlug(slug: String): Article
    fun save(article: Article): Article
}
