package pl.edu.wat.wcy.epistimi.article

import org.springframework.stereotype.Component

@Component
class ArticleStubbing(
    private val articleRepository: ArticleRepository
) {
    fun articlesExist(vararg articles: Article) {
        articles.forEach { articleRepository.save(it) }
    }
}
