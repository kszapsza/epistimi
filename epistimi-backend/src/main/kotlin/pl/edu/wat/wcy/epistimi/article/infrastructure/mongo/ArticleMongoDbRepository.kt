package pl.edu.wat.wcy.epistimi.article.infrastructure.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleMongoDbRepository : MongoRepository<ArticleMongoDbDocument, String> {
    fun findFirstBySlug(slug: String): ArticleMongoDbDocument
}
