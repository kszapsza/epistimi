package pl.edu.wat.wcy.epistimi.article.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.wat.wcy.epistimi.article.ArticleFacade
import pl.edu.wat.wcy.epistimi.article.port.ArticleRepository

@Configuration
class ArticleConfiguration {

    @Bean
    fun articleService(articleRepository: ArticleRepository): ArticleFacade {
        return ArticleFacade(articleRepository)
    }
}
