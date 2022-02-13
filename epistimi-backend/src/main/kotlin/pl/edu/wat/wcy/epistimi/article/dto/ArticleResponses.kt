package pl.edu.wat.wcy.epistimi.article.dto

import pl.edu.wat.wcy.epistimi.article.Article

data class ArticleResponse(
    val id: String,
    val title: String,
    val description: String,
) {
    companion object {
        fun fromDomain(article: Article) = ArticleResponse(
            id = article.id,
            title = article.title,
            description = article.description,
        )
    }
}

data class ArticlesResponse(
    val articles: List<ArticleResponse>
)
