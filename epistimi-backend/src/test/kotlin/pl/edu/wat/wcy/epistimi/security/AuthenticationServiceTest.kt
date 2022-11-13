package pl.edu.wat.wcy.epistimi.security

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.edu.wat.wcy.epistimi.TestData
import pl.edu.wat.wcy.epistimi.security.adapter.rest.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.user.domain.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.domain.port.UserRepository

internal class AuthenticationServiceTest : ShouldSpec({

    val userRepository = mockk<UserRepository>()
    val passwordEncoder = BCryptPasswordEncoder()

    val authenticationService = AuthenticationService(userRepository, passwordEncoder, "jwt_secret")

    should("throw an exception if user with provided username does not exist") {
        // given
        every { userRepository.findByUsername("username") } throws UserNotFoundException()

        // when
        val exception = shouldThrow<UnauthorizedException> {
            authenticationService.login(LoginRequest(username = "username", password = "123456"))
        }

        // then
        exception.message shouldBe "Invalid username or password"
    }

    should("throw an exception if user with provided username exist and password is incorrect") {
        // given
        every { userRepository.findByUsername("username") } throws UserNotFoundException()

        // when
        val exception = shouldThrow<UnauthorizedException> {
            authenticationService.login(LoginRequest(username = "username", password = "654321"))
        }

        // then
        exception.message shouldBe "Invalid username or password"
    }

    should("return a JWT token if username and password are correct") {
        // given
        every {
            userRepository.findByUsername("username")
        } returns TestData.Users.student.copy(
            username = "username",
            passwordHash = passwordEncoder.encode("123456"),
        )

        // when
        val loginResponse = authenticationService.login(LoginRequest(username = "username", password = "123456"))

        // then
        loginResponse.token.isNotBlank()
    }
})
