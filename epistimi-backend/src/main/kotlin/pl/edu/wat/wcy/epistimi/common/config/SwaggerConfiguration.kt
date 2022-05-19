package pl.edu.wat.wcy.epistimi.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfiguration {
    @Bean
    fun api(apiInfo: ApiInfo): Docket {
        return Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("pl.edu.wat.wcy.epistimi"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo)
    }

    @Bean
    fun apiInfo(): ApiInfo {
        return ApiInfo(
            "Epistimi API",
            "RESTful API for Epistimi",
            "1.0",
            null,
            Contact("Karol Szapsza", "https://kszapsza.github.io/", null),
            null,
            null,
            emptyList(),
        )
    }
}
