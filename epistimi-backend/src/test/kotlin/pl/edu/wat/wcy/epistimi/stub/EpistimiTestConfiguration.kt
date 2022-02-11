package pl.edu.wat.wcy.epistimi.stub

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pl.edu.wat.wcy.epistimi.organization.OrganizationRepository
import pl.edu.wat.wcy.epistimi.user.UserRepository

@TestConfiguration
internal class EpistimiTestConfiguration {

    @Bean
    fun organizationRepository(): OrganizationRepository = InMemoryOrganizationRepository()

    @Bean
    fun userRepository(): UserRepository = InMemoryUserRepository()

}
