package pl.edu.wat.wcy.epistimi.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.edu.wat.wcy.epistimi.security.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.security.dto.LoginResponse
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository
import java.util.Date

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${epistimi.security.jwt-secret}") private val jwtSecret: String,
    @Value("\${epistimi.security.jwt-expiry-millis}") private val jwtExpiryMillis: Long,
) {
    fun login(loginRequest: LoginRequest): LoginResponse {
        return retrieveUser(loginRequest.username)
            .also { user -> checkPassword(loginRequest.password, user.passwordHash) }
            .let { user -> LoginResponse(
                token = issueToken(user),
                firstName = user.firstName,
                lastName = user.lastName,
                role = user.role.toString(),
                username = user.username,
            ) }
    }

    private fun retrieveUser(username: String): User {
        return try {
            userRepository.findByUsername(username)
        } catch (e: UserNotFoundException) {
            throw UnauthorizedException("Invalid username or password")
        }
    }

    private fun checkPassword(rawPassword: String, encodedPassword: String) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw UnauthorizedException("Invalid username or password")
        }
    }

    private fun issueToken(user: User): String {
        return user.run {
            Jwts.builder()
                .setSubject(username)
                .claim(JwtClaims.ROLE, role)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + jwtExpiryMillis))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.toByteArray())
                .compact()
        }
    }
}
