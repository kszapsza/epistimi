package pl.edu.wat.wcy.epistimi.security

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.edu.wat.wcy.epistimi.security.dto.LoginRequest
import pl.edu.wat.wcy.epistimi.user.User
import pl.edu.wat.wcy.epistimi.user.User.Role.STUDENT
import pl.edu.wat.wcy.epistimi.user.User.Sex.MALE
import pl.edu.wat.wcy.epistimi.user.UserId
import pl.edu.wat.wcy.epistimi.user.UserNotFoundException
import pl.edu.wat.wcy.epistimi.user.UserRepository

internal class AuthenticationServiceTest : ShouldSpec({

    val userRepository = mockk<UserRepository>()
    val passwordEncoder = BCryptPasswordEncoder()

    val authenticationService = AuthenticationService(userRepository, passwordEncoder, "jwt_secret")

    val userStub = User(
        id = UserId("user_id"),
        firstName = "Jan",
        lastName = "Kowalski",
        role = STUDENT,
        username = "username",
        passwordHash = passwordEncoder.encode("123456"),
        sex = MALE,
    )

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
        every { userRepository.findByUsername("username") } returns userStub

        // when
        val loginResponse = authenticationService.login(LoginRequest(username = "username", password = "123456"))

        // then
        loginResponse.token.isNotBlank()
    }

})
