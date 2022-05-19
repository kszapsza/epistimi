package pl.edu.wat.wcy.epistimi.article.adapter.mongo

import pl.edu.wat.wcy.epistimi.article.Article
import pl.edu.wat.wcy.epistimi.article.ArticleId
import pl.edu.wat.wcy.epistimi.common.mapper.BiMapper

object ArticleDbBiMapper : BiMapper<Article, ArticleMongoDbDocument> {
    override fun toDomain(entity: ArticleMongoDbDocument) = with(entity) {
        Article(
            id = ArticleId(id!!),
            slug = slug,
            title = title,
            description = description,
        )
    }

    override fun fromDomain(domainObject: Article) = with(domainObject) {
        ArticleMongoDbDocument(
            id = id?.value,
            slug = slug,
            title = title,
            description = description,
        )
    }
}
