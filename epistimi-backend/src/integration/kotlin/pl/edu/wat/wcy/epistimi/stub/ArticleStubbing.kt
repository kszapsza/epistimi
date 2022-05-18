package pl.edu.wat.wcy.epistimi.stub

import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.article.Article
import pl.edu.wat.wcy.epistimi.article.port.ArticleRepository

@Component
internal class ArticleStubbing(
    private val articleRepository: ArticleRepository
) {
    fun articlesExist(vararg articles: Article) {
        articles.forEach { articleRepository.save(it) }
    }
}
