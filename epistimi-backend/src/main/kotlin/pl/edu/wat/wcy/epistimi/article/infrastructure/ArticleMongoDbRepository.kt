package pl.edu.wat.wcy.epistimi.article.infrastructure

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleMongoDbRepository : MongoRepository<ArticleMongoDbDocument, String>
