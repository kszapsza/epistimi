package pl.edu.wat.wcy.epistimi.article.domain

data class Article(
    val id: ArticleId? = null,
    val slug: String,
    val title: String,
    val description: String,
)

@JvmInline
value class ArticleId(
    val value: String,
)
