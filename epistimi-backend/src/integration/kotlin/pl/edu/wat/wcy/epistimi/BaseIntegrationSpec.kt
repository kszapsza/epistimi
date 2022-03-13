package pl.edu.wat.wcy.epistimi

import io.kotest.core.spec.style.ShouldSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import pl.edu.wat.wcy.epistimi.config.MongoDbConfigListener
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
            registry.add("spring.data.mongodb.uri") { ProjectConfig.mongoDbConfigListener.container.replicaSetUrl }
            registry.add("spring.data.mongodb.database") { MongoDbConfigListener.MONGODB_DATABASE_NAME }
        }
    }
}
