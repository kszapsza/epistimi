package pl.edu.wat.wcy.epistimi.article.infrastructure

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "articles")
data class ArticleMongoDbDocument(
    @Id val id: String? = null,
    @Indexed(unique = true) val slug: String,
    val title: String,
    val description: String,
)
