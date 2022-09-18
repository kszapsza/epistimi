package pl.edu.wat.wcy.epistimi

import io.kotest.core.spec.style.ShouldSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import pl.edu.wat.wcy.epistimi.config.MongoDbConfigListener
import pl.edu.wat.wcy.epistimi.config.PostgreSqlConfigListener
import pl.edu.wat.wcy.epistimi.config.ProjectConfig

@ActiveProfiles("integration")
@SpringBootTest(
    classes = [EpistimiApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
internal abstract class BaseIntegrationSpec(body: ShouldSpec.() -> Unit) : ShouldSpec(body) {
    companion object {
        @DynamicPropertySource
        @JvmStatic
        fun configure(registry: DynamicPropertyRegistry) {
            registry.apply {
                add("spring.data.mongodb.uri") { ProjectConfig.mongoDbConfigListener.container.replicaSetUrl }
                add("spring.data.mongodb.database") { MongoDbConfigListener.MONGODB_DATABASE_NAME }
                add("spring.datasource.url") { ProjectConfig.postgreSqlConfigListener.container.jdbcUrl }
                add("spring.datasource.username") { PostgreSqlConfigListener.POSTGRESQL_USERNAME }
                add("spring.datasource.password") { PostgreSqlConfigListener.POSTGRESQL_PASSWORD }
                add("spring.datasource.database") { PostgreSqlConfigListener.POSTGRESQL_DATABASE_NAME }
            }
        }
    }
}
