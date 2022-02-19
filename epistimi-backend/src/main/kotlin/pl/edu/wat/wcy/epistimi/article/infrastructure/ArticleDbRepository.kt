package pl.edu.wat.wcy.epistimi.article.infrastructure

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.article.Article
import pl.edu.wat.wcy.epistimi.article.ArticleNotFoundException
import pl.edu.wat.wcy.epistimi.article.ArticleRepository

@Repository
class ArticleDbRepository(
    val articleMongoDbRepository: ArticleMongoDbRepository
) : ArticleRepository {

    override fun findAll(): List<Article> =
        articleMongoDbRepository.findAll()
            .map { it.toDomain() }

    private fun ArticleMongoDbDocument.toDomain() = Article(
        id = this.id,
        slug = this.slug,
        title = this.title,
        description = this.description,
    )

    override fun findById(articleId: String): Article =
        articleMongoDbRepository.findById(articleId)
            .map { it.toDomain() }
            .orElseThrow { throw ArticleNotFoundException() }

    override fun findBySlug(slug: String): Article =
        try {
            articleMongoDbRepository.findFirstBySlug(slug).toDomain()
        } catch (e: EmptyResultDataAccessException) {
            throw ArticleNotFoundException()
        }
}
