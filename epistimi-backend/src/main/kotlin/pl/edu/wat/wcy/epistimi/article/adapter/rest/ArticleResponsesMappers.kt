package pl.edu.wat.wcy.epistimi.article.adapter.rest

import pl.edu.wat.wcy.epistimi.article.Article
import pl.edu.wat.wcy.epistimi.shared.mapper.FromDomainMapper

object ArticleResponseMapper : FromDomainMapper<Article, ArticleResponse> {
    override fun fromDomain(domainObject: Article) = domainObject.toResponse()
}

private fun Article.toResponse() = ArticleResponse(
    id = id!!.value,
    slug = slug,
    title = title,
    description = description,
)

object ArticlesResponseMapper : FromDomainMapper<List<Article>, ArticlesResponse> {
    override fun fromDomain(domainObject: List<Article>) =
        ArticlesResponse(articles = domainObject.map { it.toResponse() })
}
