package pl.edu.wat.wcy.epistimi.article

interface ArticleRepository {
    fun findAll(): List<Article>
    fun findById(articleId: String): Article
}
