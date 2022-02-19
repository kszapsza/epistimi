package pl.edu.wat.wcy.epistimi.article.dto

import pl.edu.wat.wcy.epistimi.article.Article

data class ArticleResponse(
    val id: String,
    val slug: String,
    val title: String,
    val description: String,
)

data class ArticlesResponse(
    val articles: List<ArticleResponse>
)
