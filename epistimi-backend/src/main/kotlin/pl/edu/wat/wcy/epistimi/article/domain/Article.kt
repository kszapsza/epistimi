package pl.edu.wat.wcy.epistimi.article.domain

data class Article(
    val id: ArticleId? = null,
    val slug: String,
    val title: String,
    val description: String,
)

// TODO: article (cover) images storage in MongoDB

@JvmInline
value class ArticleId(
    val value: String,
)
