package pl.edu.wat.wcy.epistimi.article

import org.springframework.stereotype.Service

@Service
class ArticleService(
    val articleRepository: ArticleRepository
) {
    fun getArticles(): List<Article> = articleRepository.findAll()
}
