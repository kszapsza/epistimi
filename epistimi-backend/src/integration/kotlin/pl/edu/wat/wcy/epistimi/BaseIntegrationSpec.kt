package pl.edu.wat.wcy.epistimi

import io.kotest.core.spec.style.ShouldSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import pl.edu.wat.wcy.epistimi.config.MongoListener
import pl.edu.wat.wcy.epistimi.config.ProjectConfig

@ActiveProfiles("integration")
@SpringBootTest(
    classes = [EpistimiApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
abstract class BaseIntegrationSpec(body: ShouldSpec.() -> Unit) : ShouldSpec(body) {
    companion object {
        @DynamicPropertySource
        @JvmStatic
        fun configure(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { ProjectConfig.mongoListener.container.replicaSetUrl }
            registry.add("spring.data.mongodb.database") { MongoListener.MONGODB_DATABASE_NAME }
        }
    }
}
