package pl.edu.wat.wcy.epistimi.stub

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import pl.edu.wat.wcy.epistimi.organization.domain.Organization
import pl.edu.wat.wcy.epistimi.security.AuthenticationService
import pl.edu.wat.wcy.epistimi.security.adapter.rest.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.user.domain.User
import pl.edu.wat.wcy.epistimi.user.domain.UserRole
import java.util.UUID

@Component
internal class SecurityStubbing(
    private val userStubbing: UserStubbing,
    private val authenticationService: AuthenticationService,
) {
    fun authorizationHeaderFor(
        role: UserRole,
        organization: Organization,
    ): HttpHeaders {
        return userStubbing.userExists(
            role = role,
            username = UUID.randomUUID().toString(),
            password = "123456",
            organization = organization,
        ).let { authorizationHeaderFor(it) }
    }

    fun authorizationHeaderFor(user: User): HttpHeaders {
        return tokenFor(user)
            .let { token -> HttpHeaders().apply { setBearerAuth(token) } }
    }

    private fun tokenFor(user: User): String {
        return authenticationService.login(
            LoginRequest(
                username = user.username,
                password = "123456"
            )
        ).token
    }
}
