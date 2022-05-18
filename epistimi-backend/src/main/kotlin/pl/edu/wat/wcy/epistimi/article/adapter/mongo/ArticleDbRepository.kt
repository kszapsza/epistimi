package pl.edu.wat.wcy.epistimi.article.adapter.mongo

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Repository
import pl.edu.wat.wcy.epistimi.article.Article
import pl.edu.wat.wcy.epistimi.article.ArticleNotFoundException
import pl.edu.wat.wcy.epistimi.article.port.ArticleRepository
import pl.edu.wat.wcy.epistimi.shared.mapper.DbHandlers

@Repository
class ArticleDbRepository(
    private val articleMongoDbRepository: ArticleMongoDbRepository,
) : ArticleRepository {

    override fun findAll(): List<Article> {
        return DbHandlers.handleDbMultiGet(
            mapper = ArticleDbBiMapper,
            dbCall = articleMongoDbRepository::findAll
        )
    }

    override fun findById(articleId: String): Article {
        return DbHandlers.handleDbGet(mapper = ArticleDbBiMapper) {
            articleMongoDbRepository.findById(articleId)
                .orElseThrow { ArticleNotFoundException() }
        }
    }

    override fun findBySlug(slug: String): Article {
        return try {
            DbHandlers.handleDbGet(mapper = ArticleDbBiMapper) {
                articleMongoDbRepository.findFirstBySlug(slug)
            }
        } catch (e: EmptyResultDataAccessException) {
            throw ArticleNotFoundException()
        }
    }

    override fun save(article: Article): Article {
        return DbHandlers.handleDbInsert(
            domainObject = article,
            mapper = ArticleDbBiMapper,
            dbCall = articleMongoDbRepository::save,
        )
    }
}
